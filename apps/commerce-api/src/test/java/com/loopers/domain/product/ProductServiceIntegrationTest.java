package com.loopers.domain.product;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.Gender;
import com.loopers.support.SortType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceIntegrationTest {

    @SpyBean
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("상품 저장")
    void saveProduct() {

        // arrange
        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var product1 = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                10000,
                10,
                1L
        );

        // act
        productService.save(product1);

        // assert
        verify(productJpaRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("상품 목록 조회 시 상품이 존재하지 않으면 빈 리스트를 반환한다.")
    void findAllProductsWhenNoProductsExist() {
        // arrange

        // act
        List<ProductInfo> products = productService.getProducts(SortType.PRICE_ASC);

        // assert
        assertThat(products.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("상품 목록 조회를 하면 정렬된 상품 목록이 반환된다.")
    void findAllProducts() {

        // arrange
        BrandEntity brand = brandJpaRepository.save(new BrandEntity("나이키", "나이키 입니다."));

        var product1 = new ProductEntity(
                "쫄쫄이 티셔츠",
                "그냥 운동할 때 입는 거임",
                1000,
                10,
                brand
        );
        var product2 = new ProductEntity(
                "피마이너스원 콜라보 에어포스",
                "제일 비싼 지디 콜라보 한정판 신발임",
                5000,
                20,
                brand
        );

        var product3 = new ProductEntity(
                "머큐리얼 베이퍼",
                "적당히 비싼 축구화임",
                4000,
                20,
                brand
        );

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        // act
        List<ProductInfo> products = productService.getProducts(SortType.PRICE_ASC);

        // assert
        assertAll(
                () -> assertThat(products.size()).isEqualTo(3),
                () -> assertThat(products.get(0).name()).isEqualTo("쫄쫄이 티셔츠"),
                () -> assertThat(products.get(1).name()).isEqualTo("머큐리얼 베이퍼"),
                () -> assertThat(products.get(2).name()).isEqualTo("피마이너스원 콜라보 에어포스")
        );
    }
}
