package com.loopers.domain.like;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductWithLikeCount;
import com.loopers.domain.user.*;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
                        10000,
                        10,
                        savedBrand
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
}
