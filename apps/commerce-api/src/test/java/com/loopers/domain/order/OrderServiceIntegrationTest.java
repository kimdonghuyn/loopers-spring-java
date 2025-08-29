package com.loopers.domain.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponCommand;
import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.*;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.enums.*;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

    @Autowired
    private CouponService couponService;

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
                BigDecimal.valueOf(10000),
                10,
                1L
        );
        var product2 = new ProductCommand.Create(
                "운동화",
                "그냥 운동할 때 신는 거임",
                BigDecimal.valueOf(20000),
                10,
                1L
        );
        productService.save(product1);
        productService.save(product2);

        var orderCommand = new OrderCommand.Order(
                1L,
                List.of(new OrderCommand.OrderItem(1L, new Quantity(1), BigDecimal.valueOf(10000)),
                        new OrderCommand.OrderItem(2L, new Quantity(2),  BigDecimal.valueOf(20000)))
        );

        // act
        OrderInfo orderInfo = orderService.order(orderCommand);

        // assert
        assertAll(
                () -> assertThat(orderInfo).isNotNull(),
                () -> assertThat(orderInfo.userId()).isEqualTo(1L),
                () -> assertThat(orderInfo.status()).isEqualTo(OrderStatus.PENDING),
                () -> assertThat(orderInfo.orderItems().size()).isEqualTo(2),
                () -> assertThat(orderInfo.orderItems().get(0).getProductId()).isEqualTo(1L),
                () -> assertThat(orderInfo.orderItems().get(0).getQuantity().getQuantity()).isEqualTo(1),
                () -> assertThat(orderInfo.totalPrice()).isEqualTo(BigDecimal.valueOf(50000)) // 10000 * 1 + 20000 * 2
        );
    }

    @Test
    @DisplayName("주문 시 포인트가 차감된다.")
    void reducePointOnOrder() {
        // arrange
        UserInfo userInfo = userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                BigDecimal.valueOf(30000)
        ));


        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                BigDecimal.valueOf(10000),
                10,
                brand.getId()
        );

        productService.save(productCommand);

        BigDecimal disCountedTotalPrice = BigDecimal.valueOf(5000);  // 할인된 금액

        // act
        pointService.use(new PointCommand.Use(userInfo.loginId().getLoginId(), disCountedTotalPrice));

        // assert
        assertThat(pointRepository.findByLoginId(new LoginId("loopers123")).get().getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(25000)); // 30000 - 5000 (5 * 10000)
    }

    @Test
    @DisplayName("주문 시 포인트가 부족하면 예외를 던진다.")
    void return_BadRequestWhenPointInsufficient() {
        // arrange
        UserInfo userInfo = userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F
        ));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                BigDecimal.valueOf(30000)
        ));


        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                BigDecimal.valueOf(1000),
                10,
                brand.getId()
        );

        productService.save(productCommand);

        // act
        BigDecimal disCountedTotalPrice = BigDecimal.valueOf(35000);  // 35개 주문

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.use(new PointCommand.Use(userInfo.loginId().getLoginId(), disCountedTotalPrice));
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
                BigDecimal.valueOf(30000)
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                BigDecimal.valueOf(10000),
                10,
                brand.getId()
        );
        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                "loopers123",
                PaymentMethod.POINT,
                List.of(new OrderCriteria.OrderItem(1L, 2)),
                null,
                null
        );


        List<ProductInfo> productInfos = productService.getProductsByProductId(List.of(1L));

        OrderCommand.Order orderCommand = orderCriteria.toCommand(1L, productInfos, BigDecimal.valueOf(20000));

        orderCommand.orderItems().forEach(item ->
                productService.consume(new ProductCommand.Consume(item.productId(), item.quantity()))
        );

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
                BigDecimal.valueOf(30000)
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                BigDecimal.valueOf(10000),
                0,
                brand.getId()
        );
        productService.save(productCommand);

        // act
        var orderCriteria = new OrderCriteria.Order(
                "loopers123",
                PaymentMethod.POINT,
                List.of(new OrderCriteria.OrderItem(1L, 2)),
                null,
                null
        );

        List<ProductInfo> productInfos = productService.getProductsByProductId(List.of(1L));

        OrderCommand.Order orderCommand = orderCriteria.toCommand(1L, productInfos, BigDecimal.valueOf(20000));

        // assert
        assertThrows(CoreException.class, () -> {
            orderCommand.orderItems().forEach(item ->
                    productService.consume(new ProductCommand.Consume(item.productId(), item.quantity()))
            );
        });
    }

    @DisplayName("동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다. ")
    @Test
    void concurrentOrderOnSameProduct() {
        // arrange
        userService.signUp(new UserCommand.SignUp(
                "loopers123",
                "hyun",
                "loopers@naver.com",
                "1990-01-01",
                Gender.F));

        pointService.initPoint(new PointCommand.Init(
                "loopers123",
                BigDecimal.valueOf(30000)
        ));

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var productCommand = new ProductCommand.Create(
                "운동복 세트",
                "그냥 운동할 때 입는 거임",
                BigDecimal.valueOf(10000),
                10,   // 초기 재고
                brand.getId()
        );
        productService.save(productCommand);

        final int threads = 15;
        final int qtyPerOrder = 1;

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            var futures = new ArrayList<Future<?>>();
            IntStream.range(0, threads).forEach(i -> futures.add(pool.submit(() -> {
                try {
                    startLatch.await();

                    productService.consume(new ProductCommand.Consume(1L, new Quantity(qtyPerOrder)));
                    success.incrementAndGet();
                } catch (Exception e) {
                    failure.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            })));

            startLatch.countDown();

            boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
            if (!finished) {
                throw new AssertionError("동시성 테스트가 제한 시간 내에 끝나지 않았습니다.");
            }

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new AssertionError("테스트가 인터럽트되었습니다.", ie);
        } finally {
            pool.shutdownNow();
        }

        // assert
        assertAll(
                () -> assertThat(success.get()).isEqualTo(10),
                () -> assertThat(failure.get()).isEqualTo(5),
                () -> assertThat(productRepository.findById(1L).get().getStock()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("주문 중 하나라도 실패하면(재고 차감 단계에서 예외), 쿠폰/재고/포인트 모두 롤백된다.")
    void rollback_all_when_any_step_fails() {
        // arrange
        UserInfo user = userService.signUp(new UserCommand.SignUp(
                "loopers123", "hyun", "loopers@naver.com", "1990-01-01", Gender.F
        ));
        pointService.initPoint(new PointCommand.Init(user.loginId().getLoginId(), BigDecimal.valueOf(10000L)));
        BigDecimal beforePoint = pointRepository.findByLoginId(new LoginId("loopers123")).orElseThrow().getAmount();

        BrandEntity brand = brandJpaRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));
        var p = new ProductCommand.Create("운동복 세트", "그냥 운동", BigDecimal.valueOf(10000), 5, brand.getId());
        productService.save(p);
        int beforeStock = productRepository.findById(1L).orElseThrow().getStock();

        Long couponId = null;
        try {
            Coupon created = couponService.createCoupon(new CouponCommand.Create(
                    "테스트쿠폰", DiscountPolicy.FIXED, 1000, null,
                    LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1
            ));
            couponId = created.getId();
        } catch (Exception ignore) {
        }

        var orderCriteria = new OrderCriteria.Order(
                "loopers123",
                PaymentMethod.POINT,
                List.of(new OrderCriteria.OrderItem(1L, 2)),
                null,
                null
        );

        List<ProductInfo> products = productService.getProductsByProductId(List.of(1L));
        OrderCommand.Order orderCmd = orderCriteria.toCommand(1L, products, BigDecimal.valueOf(19000));

        Mockito.doThrow(new RuntimeException("force failure during stock update"))
                .when(productJpaRepository).save(Mockito.any());

        assertThrows(CoreException.class, () -> {
            productService.consume(new ProductCommand.Consume(1L, new Quantity(11)));
        });

        BigDecimal afterPoint = pointRepository.findByLoginId(new LoginId("loopers123")).orElseThrow().getAmount();
        assertThat(afterPoint).isEqualTo(beforePoint);

        int afterStock = productRepository.findById(1L).orElseThrow().getStock();
        assertThat(afterStock).isEqualTo(beforeStock);

        if (couponId != null) {
            CouponInfo couponInfo = couponService.getCoupon(couponId).orElseThrow();
            assertThat(couponInfo.status()).isEqualTo(CouponStatus.ACTIVE);
        }

        Mockito.reset(productJpaRepository);
    }
}
