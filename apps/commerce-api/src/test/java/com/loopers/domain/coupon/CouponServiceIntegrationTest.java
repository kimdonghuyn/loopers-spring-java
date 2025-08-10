package com.loopers.domain.coupon;

import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponServiceIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("쿠폰 등록할 때, ")
    @Nested
    class CreateCoupon {

        @DisplayName("유효한 쿠폰 정보가 주어지면, 쿠폰을 생성한다.")
        @Test
        void createCoupon_whenValidCouponInfoGiven() {
            // arrange
            CouponCommand.Create createCommand = new CouponCommand.Create(
                    "Test Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    10
            );


            // act
            Coupon coupon = couponService.createCoupon(createCommand);

            // assert
            assertAll(
                    () -> assertThat(coupon.getName()).isEqualTo("Test Coupon"),
                    () -> assertThat(coupon.getDiscountPolicy()).isEqualTo(DiscountPolicy.FIXED),
                    () -> assertThat(coupon.getDiscountAmount()).isEqualTo(1000),
                    () -> assertThat(coupon.getDiscountRate()).isNull(),
                    () -> assertThat(coupon.getIssuedAt()).isBeforeOrEqualTo(LocalDateTime.now()),
                    () -> assertThat(coupon.getExpiredAt()).isAfter(LocalDateTime.now()),
                    () -> assertThat(coupon.getQuantity()).isEqualTo(10)
            );
        }
    }

    @DisplayName("쿠폰 발행할 때, ")
    @Nested
    class PublishCoupon {

        @DisplayName("유효한 쿠폰 ID가 주어지면, 쿠폰을 발행한다.")
        @Test
        void publishCoupon_whenValidCouponIdGiven() {
            // arrange
            CouponCommand.Create createCommand = new CouponCommand.Create(
                    "Test Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    10
            );
            Coupon coupon = couponService.createCoupon(createCommand);
            Long couponId = coupon.getId();

            // act
            couponService.publishCoupon(couponId);

            // assert
            Coupon publishedCoupon = couponRepository.findById(couponId).orElseThrow();
            assertAll(
                    () -> assertThat(publishedCoupon.getName()).isEqualTo("Test Coupon"),
                    () -> assertThat(publishedCoupon.getDiscountPolicy()).isEqualTo(DiscountPolicy.FIXED),
                    () -> assertThat(publishedCoupon.getDiscountAmount()).isEqualTo(1000),
                    () -> assertThat(publishedCoupon.getDiscountRate()).isNull(),
                    () -> assertThat(publishedCoupon.getIssuedAt()).isBeforeOrEqualTo(LocalDateTime.now()),
                    () -> assertThat(publishedCoupon.getExpiredAt()).isAfter(LocalDateTime.now()),
                    () -> assertThat(publishedCoupon.getQuantity()).isEqualTo(9)
            );
        }

        @DisplayName("존재하지 않는 쿠폰 ID가 주어지면, BAD_REQUEST를 발생시킨다.")
        @Test
        void publishCoupon_whenInvalidCouponIdGiven() {
            // arrange
            Long invalidCouponId = 999L;

            // act
            CoreException exception = assertThrows(CoreException.class, () -> couponService.publishCoupon(invalidCouponId));

            // assert
            assertAll(
                    () -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST),
                    () -> assertThat(exception.getMessage()).isEqualTo("쿠폰이 존재하지 않습니다.")
            );
        }

        @DisplayName("재고가 없는 쿠폰을 발행하려고 하면, IllegalArgumentException을 발생시킨다.")
        @Test
        void publishCoupon_whenOutOfStockCouponGiven() {
            // arrange
            CouponCommand.Create createCommand = new CouponCommand.Create(
                    "Test Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    1
            );
            Coupon coupon = couponService.createCoupon(createCommand);
            Long couponId = coupon.getId();

            couponService.publishCoupon(couponId);

            // act
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> couponService.publishCoupon(couponId));

            // assert
            assertAll(
                    () -> assertThat(exception.getClass()).isEqualTo(IllegalArgumentException.class),
                    () -> assertThat(exception.getMessage()).isEqualTo("쿠폰 수량이 부족합니다.")
            );
        }
    }

    @DisplayName("쿠폰 조회할 때, ")
    @Nested
    class FindCoupon {

        @DisplayName("유효한 쿠폰 ID가 주어지면, 쿠폰을 조회한다.")
        @Test
        void findCoupon_whenValidCouponIdGiven() {
            // arrange
            CouponCommand.Create createCommand = new CouponCommand.Create(
                    "Test Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    10
            );
            Coupon coupon = couponService.createCoupon(createCommand);
            Long couponId = coupon.getId();

            // act
            Optional<Coupon> foundCoupon = couponRepository.findById(couponId);

            // assert
            assertAll(
                    () -> assertThat(foundCoupon).isPresent(),
                    () -> assertThat(foundCoupon.get().getName()).isEqualTo("Test Coupon"),
                    () -> assertThat(foundCoupon.get().getDiscountPolicy()).isEqualTo(DiscountPolicy.FIXED),
                    () -> assertThat(foundCoupon.get().getDiscountAmount()).isEqualTo(1000),
                    () -> assertThat(foundCoupon.get().getDiscountRate()).isNull(),
                    () -> assertThat(foundCoupon.get().getIssuedAt()).isBeforeOrEqualTo(LocalDateTime.now()),
                    () -> assertThat(foundCoupon.get().getExpiredAt()).isAfter(LocalDateTime.now()),
                    () -> assertThat(foundCoupon.get().getQuantity()).isEqualTo(10)
            );
        }

        @DisplayName("존재하지 않는 쿠폰 ID가 주어지면, 빈 값을 반환한다.")
        @Test
        void findCoupon_whenInvalidCouponIdGiven() {
            // arrange
            Long invalidCouponId = 999L;

            // act
            Optional<CouponInfo> foundCoupon = couponService.getCoupon(invalidCouponId);

            // assert
            assertThat(foundCoupon).isEmpty();
        }
    }

    @DisplayName("쿠폰 적용할 때, ")
    @Nested
    class ApplyCoupon {

        @DisplayName("유효한 할인 정책과 금액이 주어지면, 쿠폰을 적용한다.")
        @Test
        void applyCoupon_whenValidDiscountPolicyAndAmountGiven() {
            // arrange
            CouponCommand.Create createCommand = new CouponCommand.Create(
                    "Test Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    10
            );
            Coupon coupon = couponService.createCoupon(createCommand);

            // act
            int discountedPrice = couponService.calculateDiscountPrice(
                    List.of(1000),
                    1L
            );

            // assert
            assertThat(discountedPrice).isEqualTo(0);
        }

        @DisplayName("만료된 쿠폰이 주어지면, 예외를 발생시킨다.")
        @Test
        void applyCoupon_whenExpiredCouponGiven() {
            // arrange
            Coupon coupon = couponService.createCoupon(new CouponCommand.Create(
                    "Expired Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1),
                    10
            ));

            // act & assert
            CoreException ex = assertThrows(CoreException.class, () ->
                    couponService.calculateDiscountPrice(List.of(5000), coupon.getId())
            );
            assertThat(ex.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            assertThat(ex.getMessage()).isEqualTo("사용기한이 지난 쿠폰입니다.");
        }


        @DisplayName("동시에 여러 요청이 와도 쿠폰은 단 한 번만 사용된다.")
        @Test
        void applyCoupon_concurrent_singleUse() throws Exception {
            // arrange
            Coupon coupon = couponService.createCoupon(new CouponCommand.Create(
                    "Concurrent Coupon",
                    DiscountPolicy.FIXED,
                    1000,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    10
            ));
            Long couponId = coupon.getId();

            couponService.publishCoupon(couponId);

            int threads = 10;
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            CountDownLatch ready = new CountDownLatch(threads);
            CountDownLatch start = new CountDownLatch(1);
            CountDownLatch done = new CountDownLatch(threads);
            AtomicInteger success = new AtomicInteger(0);
            AtomicInteger fail = new AtomicInteger(0);
            CopyOnWriteArrayList<Throwable> errors = new CopyOnWriteArrayList<>();

            // act
            for (int i = 0; i < threads; i++) {
                pool.submit(() -> {
                    try {
                        ready.countDown();
                        start.await();
                        try {
                            couponService.calculateDiscountPrice(List.of(5000), couponId);
                            success.incrementAndGet();
                        } catch (Exception e) {
                            fail.incrementAndGet();
                            errors.add(e);
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                });
            }

            if (!ready.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Threads not ready in time");
            }
            start.countDown();
            done.await(15, TimeUnit.SECONDS);
            pool.shutdownNow();

            // assert
            assertThat(success.get()).isEqualTo(1);
            assertThat(fail.get()).isEqualTo(threads - 1);

            for (Throwable t : errors) {
                boolean expected =
                        (t instanceof CoreException core && core.getErrorType() == ErrorType.BAD_REQUEST &&
                                ("쿠폰이 활성화 상태가 아닙니다.".equals(core.getMessage()) ||
                                 "사용기한이 지난 쿠폰입니다.".equals(core.getMessage())))
                        || (t instanceof ObjectOptimisticLockingFailureException)
                        || (t instanceof OptimisticLockException);
                assertThat(expected).isTrue();
            }

            Coupon after = couponRepository.findById(couponId).orElseThrow();
            assertThat(after.getStatus()).isEqualTo(CouponStatus.INACTIVE);
        }
    }


}
