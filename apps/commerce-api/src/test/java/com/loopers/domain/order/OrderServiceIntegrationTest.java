package com.loopers.domain.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.point.*;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.*;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.Gender;
import com.loopers.support.OrderStatus;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceIntegrationTest {

    @SpyBean
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private PointService pointService;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("상품 주문 성공 시 주문 정보가 반환된다.")
    void saveOrder() {
        // arrange
        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var product1 = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                10000,
                10,
                1L
        );
        var product2 = new ProductCommand.Create(
                "운동화",
                "그냥 운동할 때 신는 거임",
                20000,
                10,
                1L
        );
        productService.save(product1);
        productService.save(product2);

        var orderCommand = new OrderCommand.Order(
                1L,
                List.of(new OrderCommand.OrderItem(1L, new Quantity(1), 10000),
                        new OrderCommand.OrderItem(2L, new Quantity(2), 20000))
        );

        // act
        OrderInfo orderInfo = orderService.order(orderCommand);

        // assert
        assertAll(
                () -> assertThat(orderInfo).isNotNull(),
                () -> assertThat(orderInfo.userId()).isEqualTo(1L),
                () -> assertThat(orderInfo.status()).isEqualTo(OrderStatus.COMPLETED),
                () -> assertThat(orderInfo.orderItems().size()).isEqualTo(2),
                () -> assertThat(orderInfo.orderItems().get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(orderInfo.orderItems().get(0).getQuantity().getQuantity()).isEqualTo(1),
                () -> assertThat(orderInfo.totalPrice()).isEqualTo(50000) // 10000 * 1 + 20000 * 2
        );
    }

    @Test
    @DisplayName("주문 시 포인트가 차감된다.")
    void reducePointOnOrder() {
        // arrange
        userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                30000L
        ));


        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                1000,
                10,
                brand.getId()
        );

        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                1L,
                "loopers123",
                List.of(new OrderCriteria.OrderItem(1L, 5))
        );

        OrderResult orderResult = orderFacade.order(orderCriteria);

        // assert
        assertThat(pointRepository.findByLoginId(new LoginId("loopers123")).get().getAmount())
                .isEqualTo(25000L); // 30000 - 5000 (5 * 10000)
    }

    @Test
    @DisplayName("주문 시 포인트가 부족하면 예외를 던진다.")
    void return_BadRequestWhenPointInsufficient() {
        // arrange
        userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                30000L
        ));


        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                1000,
                10,
                brand.getId()
        );

        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                1L,
                "loopers123",
                List.of(new OrderCriteria.OrderItem(1L, 5))
        );

        OrderResult orderResult = orderFacade.order(orderCriteria);

        // assert
        assertThrows(CoreException.class, () -> {
            orderFacade.order(new OrderCriteria.Order(
                    1L,
                    "loopers123",
                    List.of(new OrderCriteria.OrderItem(1L, 6)) // 6개 주문 시 포인트 부족
            ));
        });
    }

    @Test
    @DisplayName("주문 시 재고가 차감된다.")
    void reduceStockOnOrder() {
        // arrange
        userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                30000L
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                10000,
                10,
                brand.getId()
        );
        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                1L,
                "loopers123",
                List.of(new OrderCriteria.OrderItem(1L, 2))
        );

        OrderResult orderResult = orderFacade.order(orderCriteria);

        // assert
        assertThat(productRepository.findById(1L).get().getStock()).isEqualTo(8); // 10 - 2
    }


    @Test
    @DisplayName("주문 시 재고가 부족하면 예외를 던진다.")
    void return_BadRequestWhenStockInsufficient() {
        // arrange
        userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                30000L
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                10000,
                10,
                brand.getId()
        );
        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                1L,
                "loopers123",
                List.of(new OrderCriteria.OrderItem(1L, 2))
        );

        OrderResult orderResult = orderFacade.order(orderCriteria);

        // assert
        assertThrows(CoreException.class, () -> {
            orderFacade.order(new OrderCriteria.Order(
                    1L,
                    "loopers123",
                    List.of(new OrderCriteria.OrderItem(1L, 9)) // 재고 부족
            ));
        });
    }
}
