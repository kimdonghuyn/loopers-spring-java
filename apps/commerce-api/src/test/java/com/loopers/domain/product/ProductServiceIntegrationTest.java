package com.loopers.domain.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.like.LikeCommand;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.enums.Gender;
import com.loopers.support.enums.SortType;
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
    private LikeService likeService;

    @Autowired
    private UserService userService;

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
                brand.getId()
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
                brand.getId()
        );
        var product2 = new ProductEntity(
                "피마이너스원 콜라보 에어포스",
                "제일 비싼 지디 콜라보 한정판 신발임",
                5000,
                20,
                brand.getId()
        );

        var product3 = new ProductEntity(
                "머큐리얼 베이퍼",
                "적당히 비싼 축구화임",
                4000,
                20,
                brand.getId()
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

    @Test
    @DisplayName("상품 조회 시 상품이 존재하지 않으면 예외가 발생한다.")
    void findProductByIdWhenProductDoesNotExist() {
        // arrange
        Long nonExistentProductId = 999L;

        // act & assert
        CoreException exception = assertThrows(CoreException.class, () -> productService.getProduct(nonExistentProductId));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @Test
    @DisplayName("상품 조회 시 상품이 존재하면 상품 정보를 반환한다.")
    @Transactional
    void findProductByIdWhenProductExists() {
        // arrange
        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        ProductEntity product = new ProductEntity(
                "운동화",
                "편한 운동화",
                30000,
                5,
                brand.getId()
        );
        ProductEntity savedProduct = productRepository.save(product);

        // act
        ProductInfo productInfo = productService.getProduct(savedProduct.getId());

        // assert
        assertAll(
                () -> assertThat(productInfo.name()).isEqualTo("운동화"),
                () -> assertThat(productInfo.description()).isEqualTo("편한 운동화"),
                () -> assertThat(productInfo.price()).isEqualTo(30000),
                () -> assertThat(productInfo.stock()).isEqualTo(5),
                () -> assertThat(productInfo.likeCount()).isEqualTo(0) // 초기 좋아요 수는 0
        );
    }

    @Test
    @DisplayName("여러 상품 ID로 상품을 조회 시 성공하면 상품 정보 리스트를 반환한다.")
    void findProductsByProductIds() {
        // arrange
        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        ProductEntity product1 = new ProductEntity("운동화1", "설명1", 10000, 10, brand.getId());
        ProductEntity product2 = new ProductEntity("운동화2", "설명2", 20000, 20, brand.getId());
        ProductEntity product3 = new ProductEntity("운동화3", "설명3", 30000, 30, brand.getId());

        ProductEntity savedProduct1 = productRepository.save(product1);
        ProductEntity savedProduct2 = productRepository.save(product2);
        ProductEntity savedProduct3 = productRepository.save(product3);

        // act
        List<ProductInfo> products = productService.getProductsByProductId(List.of(savedProduct1.getId(), savedProduct2.getId()));

        // assert
        assertAll(
                () -> assertThat(products.size()).isEqualTo(2),
                () -> assertThat(products.get(0).name()).isEqualTo("운동화1"),
                () -> assertThat(products.get(1).name()).isEqualTo("운동화2"),
                () -> assertThat(products.get(0).price()).isEqualTo(10000),
                () -> assertThat(products.get(1).price()).isEqualTo(20000),
                () -> assertThat(products.get(0).stock()).isEqualTo(10),
                () -> assertThat(products.get(1).stock()).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("내가 좋아요한 상품 조회 시 좋아요한 상품 목록을 반환한다.")
    void findLikedProducts() {
        // arrange
        UserInfo user = userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        ProductEntity product1 = new ProductEntity("운동화1", "설명1", 10000, 10, brand.getId());
        ProductEntity product2 = new ProductEntity("운동화2", "설명2", 20000, 20, brand.getId());
        ProductEntity product3 = new ProductEntity("운동화3", "설명3", 30000, 30, brand.getId());

        ProductEntity savedProduct1 = productRepository.save(product1);
        ProductEntity savedProduct2 = productRepository.save(product2);
        ProductEntity savedProduct3 = productRepository.save(product3);

        // act
        likeService.like(new LikeCommand.Like(
                user.userId(),
                savedProduct1.getId()
        ));

        likeService.like(new LikeCommand.Like(
                user.userId(),
                savedProduct2.getId()
        ));

        likeService.unlike(new LikeCommand.Like(
                user.userId(),
                savedProduct2.getId()
        ));

        // assert
        List<ProductInfo> likedProducts = productService.getLikedProducts(user.userId());

        assertAll(
                () -> assertThat(likedProducts.size()).isEqualTo(1),
                () -> assertThat(likedProducts.get(0).name()).isEqualTo("운동화1"),
                () -> assertThat(likedProducts.get(0).likeCount()).isEqualTo(1)
        );
    }
}
