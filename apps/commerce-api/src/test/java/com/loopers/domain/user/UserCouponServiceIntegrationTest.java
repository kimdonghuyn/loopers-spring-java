package com.loopers.domain.user;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponService;
import com.loopers.infrastructure.user.UserCouponJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;
import com.loopers.support.enums.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCouponServiceIntegrationTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @BeforeEach
    void setUp() {
        // 유저 생성
        userRepository.save(new UserEntity(
                new LoginId("testuser"),
                "test",
                new Email("test@naver.com"),
                new Birth("2000-01-01"),
                Gender.F
        ));

        // 쿠폰 생성
        couponRepository.save(new Coupon(
                "Test Coupon",
                DiscountPolicy.FIXED,
                1000,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                10
        ));

        // 사용자에게 쿠폰 발급
        UserCoupon userCoupon = UserCoupon.create(
                1L,
                1L
        );
        userCouponJpaRepository.save(userCoupon);
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("사용자에게 발급된 쿠폰 조회하면 사용자 쿠폰 정보가 반환된다.")
    @Test
    void getUserCouponsTest() {
        //arrange
        UserCouponCommand.GetUserCoupon criteria = new UserCouponCommand.GetUserCoupon(1L);

        //act
        UserCouponInfo userCouponInfo = userCouponService.getUserCoupon(criteria);

        //assert
        assertAll(
                () -> assertThat(userCouponInfo.couponId()).isEqualTo(1L),
                () -> assertThat(userCouponInfo.userId()).isEqualTo(1L),
                () -> assertThat(userCouponInfo.status()).isEqualTo(CouponStatus.ACTIVE)
        );
    }

    @DisplayName("사용자 쿠폰 사용하면 쿠폰 상태가 INACTIVE로 변경된다.")
    @Test
    void useUserCouponTest() {
        // arrange
        LocalDateTime fixedNow = LocalDateTime.now();
        UserCouponCommand.UseCoupon command = new UserCouponCommand.UseCoupon(1L);

        // act
        userCouponService.useCoupon(command);

        // assert
        UserCouponCommand.GetUserCoupon getCommand = new UserCouponCommand.GetUserCoupon(1L);
        UserCouponInfo userCoupon = userCouponService.getUserCoupon(getCommand);
        assertAll(
                () -> assertThat(userCoupon.status().name()).isEqualTo(CouponStatus.INACTIVE.name()),
                () -> assertThat(userCoupon.usedAt()).isAfter(fixedNow.minusSeconds(1)).isBefore(fixedNow.plusSeconds(1))
        );
    }

    /*
    TODO: 동시성 테스트 추후 해결 필요

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
        CountDownLatch latch = new CountDownLatch(threads);
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
                        userCouponService.useCoupon(new UserCouponCommand.UseCoupon(couponId));
                        success.incrementAndGet();
                    } catch (Exception e) {
                        fail.incrementAndGet();
                        errors.add(e);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        if (!ready.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Threads not ready in time");
        }
        start.countDown();
        latch.await(15, TimeUnit.SECONDS);
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
    }*/
}
