package com.loopers.domain.like;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductWithLikeCount;
import com.loopers.domain.user.*;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.enums.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeServiceIntegrationTest {
    @Autowired
    private LikeService likeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private static final UserEntity user = new UserEntity(
            new LoginId("loopers"),
            "hyun",
            new Email("loopers@naver.com"),
            new Birth("2000-10-10"),
            Gender.F);

    private static final BrandEntity brand = new BrandEntity(
            "아디다스",
            "adidas is never die"
    );

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        BrandEntity savedBrand = brandJpaRepository.save(brand);
        productRepository.save(new ProductEntity(
                        "아디다스 운동화",
                        "운동할 때 신는 운동화임",
                        BigDecimal.valueOf(10000),
                        10,
                        savedBrand.getId()
                )
        );
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }


    @Test
    @DisplayName("좋아요를 여러번 눌러도 좋아요 수는 1만 증가한다.")
    void increaseJustOnce_whenEverLikeMultipleTimes() {
        // arrange
        UserEntity savedUser = userRepository.findByLoginId(new LoginId("loopers"))
                .orElseThrow();
        ProductEntity savedProduct = productRepository.findAllById(List.of(1L)).get(0).product();


        // act
        likeService.like(new LikeCommand.Like(savedUser.getId(), savedProduct.getId()));
        likeService.like(new LikeCommand.Like(savedUser.getId(), savedProduct.getId()));

        // assert
        ProductWithLikeCount productWithLikeCount = productRepository.findDetailById(savedProduct.getId());
        assertThat(productWithLikeCount.likeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 취소를 여러번 눌러도 좋아요 수는 1만 감소한다.")
    void decreaseJustOnce_whenEverUnlikeMultipleTimes() {
        // arrange
        UserEntity savedUser = userRepository.findByLoginId(new LoginId("loopers"))
                .orElseThrow();
        ProductEntity savedProduct = productRepository.findAllById(List.of(1L)).get(0).product();
        likeService.like(new LikeCommand.Like(savedUser.getId(), savedProduct.getId()));

        // act
        likeService.unlike(new LikeCommand.Like(savedUser.getId(), savedProduct.getId()));
        likeService.unlike(new LikeCommand.Like(savedUser.getId(), savedProduct.getId()));

        // assert
        ProductWithLikeCount productWithLikeCount = productRepository.findDetailById(savedProduct.getId());
        assertThat(productWithLikeCount.likeCount()).isEqualTo(0);
    }


    @Test
    @DisplayName("동일한 상품에 대해 여러명이 좋아요/싫어요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.")
    void likeAndUnlikeByMultipleUsers() throws Exception {
        // arrange
        // arrange
        List<UserEntity> savedUsers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            UserEntity userEntity = new UserEntity(
                    new LoginId("user" + i),
                    "User " + (i == 10 ? "Ten" : " " + i),
                    new Email("user" + i + "@loopers.com"),
                    new Birth("2000-01-" + String.format("%02d", i)),
                    Gender.F
            );
            savedUsers.add(userRepository.save(userEntity));
        }

        ProductEntity savedProduct = productRepository.findAllById(List.of(1L)).get(0).product();
        Long productId = savedProduct.getId();

        // act 1: 좋아요 10명 동시
        runConcurrent(savedUsers.size(),
                userIdx -> likeService.like(new LikeCommand.Like(savedUsers.get(userIdx).getId(), productId))
        );

        // then 1
        assertThat(productRepository.findDetailById(productId).likeCount()).isEqualTo(10);

        // act 2: 싫어요 3명 동시
        runConcurrent(3,
                userIdx -> likeService.unlike(new LikeCommand.Like(savedUsers.get(userIdx).getId(), productId))
        );

        // then 2
        assertThat(productRepository.findDetailById(productId).likeCount()).isEqualTo(7);
    }

    /**
     * 지정된 개수의 쓰레드를 동시에 시작시켜 작업을 실행한다.
     * @param threads 쓰레드 수
     * @param task    인덱스를 인자로 받는 작업
     */
    private void runConcurrent(int threads, IntConsumer task) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch readyLatch = new CountDownLatch(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch  = new CountDownLatch(threads);

        IntStream.range(0, threads).forEach(i ->
                pool.submit(() -> {
                    try {
                        readyLatch.countDown();
                        startLatch.await();
                        task.accept(i);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                })
        );

        if (!readyLatch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Threads not ready in time");
        }
        startLatch.countDown();
        if (!doneLatch.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Tasks did not finish in time");
        }
        pool.shutdownNow();
    }
}
