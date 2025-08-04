package com.loopers.domain.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductResult;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.like.LikeEntity;
import com.loopers.domain.like.LikeRepository;
import com.loopers.support.SortType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductFacadeTest {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        brandRepository.save(new BrandEntity("브랜드1", "브랜드 설명"));
        productRepository.save(new ProductEntity("상품1", "설명1", 5000, 5, 1L));
        productRepository.save(new ProductEntity("상품2", "설명2", 10000, 10, 1L));
        productRepository.save(new ProductEntity("상품3", "설명3", 20000, 20, 1L));
    }

    @Test
    @DisplayName("정렬 조건(최신순)에 따라 상품 목록을 조회할 수 있다")
    void getProducts_sorted_success() {
        // Arrange
        SortType sortType = SortType.LATEST;

        // Act
        var products = productFacade.getProducts(sortType);

        // Assert
        assertAll(
                () -> assertThat(products).isNotNull(),
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).brandId()).isNotNull(),
                () -> assertThat(products.get(1).brandId()).isNotNull(),
                () -> assertThat(products.get(0).name()).isEqualTo("상품2"),
                () -> assertThat(products.get(1).name()).isEqualTo("상품1")
        );
    }

    @Test
    @DisplayName("정렬 조건(가격순 - 오름차순)에 따라 상품 목록을 조회할 수 있다")
    void getProducts_sortedByPrice_success() {
        // Arrange
        SortType sortType = SortType.PRICE_ASC;

        // Act
        var products = productFacade.getProducts(sortType);

        // Assert
        assertAll(
                () -> assertThat(products).isNotNull(),
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).brandId()).isNotNull(),
                () -> assertThat(products.get(1).brandId()).isNotNull(),
                () -> assertThat(products.get(0).name()).isEqualTo("상품1"),
                () -> assertThat(products.get(1).name()).isEqualTo("상품2")
        );
    }

    @Test
    @DisplayName("정렬 조건(좋아요순 - 내림차순)에 따라 상품 목록을 조회할 수 있다")
    void getProducts_sortedByLikes_success() {
        // Arrange
        likeRepository.save(new LikeEntity(1L, 1L));
        likeRepository.save(new LikeEntity(2L, 1L));
        likeRepository.save(new LikeEntity(3L, 1L));
        likeRepository.save(new LikeEntity(1L, 2L));

        SortType sortType = SortType.LIKES_DESC;

        // Act
        var products = productFacade.getProducts(sortType);

        // Assert
        assertAll(
                () -> assertThat(products).isNotNull(),
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).brandId()).isNotNull(),
                () -> assertThat(products.get(1).brandId()).isNotNull(),
                () -> assertThat(products.get(0).name()).isEqualTo("상품1"),
                () -> assertThat(products.get(1).name()).isEqualTo("상품2")
        );
    }


    @Test
    @DisplayName("단일 상품을 조회할 수 있다")
    void getProduct_success() {
        Long existingProductId = 1L;
        ProductResult result = productFacade.getProduct(existingProductId);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("좋아요한 상품 목록을 조회할 수 있다")
    void getLikedProducts_success() {
        Long userId = 1L;
        likeRepository.save(new LikeEntity(userId, 1L));
        likeRepository.save(new LikeEntity(userId, 2L));

        var liked = productFacade.getLikedProducts(userId);
        assertAll(
                () -> assertThat(liked).isNotNull(),
                () -> assertThat(liked).hasSize(2),
                () -> assertThat(liked.get(0).brandId()).isNotNull(),
                () -> assertThat(liked.get(1).brandId()).isNotNull(),
                () -> assertThat(liked.get(0).name()).isEqualTo("상품1"),
                () -> assertThat(liked.get(1).name()).isEqualTo("상품2")
        );
    }
}
