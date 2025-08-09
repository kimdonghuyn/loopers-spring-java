package com.loopers.domain.user;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
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
}
