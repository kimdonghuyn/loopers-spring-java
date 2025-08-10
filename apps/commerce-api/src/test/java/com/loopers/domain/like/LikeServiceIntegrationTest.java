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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        // arrange: 유저 10명 생성/저장
        List<UserEntity> users = List.of(
                new UserEntity(new LoginId("user1"), "User One",  new Email("user1@loopers.com"), new Birth("2000-01-01"), Gender.F),
                new UserEntity(new LoginId("user2"), "User Two",  new Email("user2@loopers.com"), new Birth("2000-01-02"), Gender.F),
                new UserEntity(new LoginId("user3"), "User Three",new Email("user3@loopers.com"), new Birth("2000-01-03"), Gender.F),
                new UserEntity(new LoginId("user4"), "User Four", new Email("user4@loopers.com"), new Birth("2000-01-04"), Gender.F),
                new UserEntity(new LoginId("user5"), "User Five", new Email("user5@loopers.com"), new Birth("2000-01-05"), Gender.F),
                new UserEntity(new LoginId("user6"), "User Six",  new Email("user6@loopers.com"), new Birth("2000-01-06"), Gender.F),
                new UserEntity(new LoginId("user7"), "User Seven",new Email("user7@loopers.com"), new Birth("2000-01-07"), Gender.F),
                new UserEntity(new LoginId("user8"), "User Eight",new Email("user8@loopers.com"), new Birth("2000-01-08"), Gender.F),
                new UserEntity(new LoginId("user9"), "User Nine", new Email("user9@loopers.com"), new Birth("2000-01-09"), Gender.F),
                new UserEntity(new LoginId("user10"),"User Ten",  new Email("user10@loopers.com"),new Birth("2000-01-10"), Gender.F)
        );
        List<UserEntity> savedUsers = users.stream()
                .map(userRepository::save)
                .toList();

        // 테스트용 상품 조회 (기존 테스트 스타일 유지)
        ProductEntity savedProduct = productRepository.findAllById(List.of(1L)).get(0).product();
        Long productId = savedProduct.getId();

        // act 1: 10명이 동시에 좋아요
        int threads1 = savedUsers.size();
        ExecutorService pool1 = Executors.newFixedThreadPool(threads1);
        CountDownLatch ready1 = new CountDownLatch(threads1);
        CountDownLatch start1 = new CountDownLatch(1);
        CountDownLatch done1 = new CountDownLatch(threads1);

        for (UserEntity u : savedUsers) {
            pool1.submit(() -> {
                try {
                    ready1.countDown();
                    start1.await();
                    likeService.like(new LikeCommand.Like(u.getId(), productId));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done1.countDown();
                }
            });
        }
        if (!ready1.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("Phase1 threads not ready");
        start1.countDown();
        done1.await(10, TimeUnit.SECONDS);
        pool1.shutdownNow();

        // then 1: 좋아요 수 = 10
        ProductWithLikeCount afterLike = productRepository.findDetailById(productId);
        assertThat(afterLike.likeCount()).isEqualTo(10);

        // act 2: 3명이 동시에 싫어요(취소) — user1, user2, user3
        List<UserEntity> toUnlike = savedUsers.subList(0, 3);
        int threads2 = toUnlike.size();
        ExecutorService pool2 = Executors.newFixedThreadPool(threads2);
        CountDownLatch ready2 = new CountDownLatch(threads2);
        CountDownLatch start2 = new CountDownLatch(1);
        CountDownLatch done2 = new CountDownLatch(threads2);

        for (UserEntity u : toUnlike) {
            pool2.submit(() -> {
                try {
                    ready2.countDown();
                    start2.await();
                    likeService.unlike(new LikeCommand.Like(u.getId(), productId));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done2.countDown();
                }
            });
        }
        if (!ready2.await(5, TimeUnit.SECONDS)) throw new IllegalStateException("Phase2 threads not ready");
        start2.countDown();
        done2.await(10, TimeUnit.SECONDS);
        pool2.shutdownNow();

        // then 2: 최종 좋아요 수 = 10 - 3 = 7
        ProductWithLikeCount afterUnlike = productRepository.findDetailById(productId);
        assertThat(afterUnlike.likeCount()).isEqualTo(7);
    }
}
