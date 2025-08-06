package com.loopers.domain.coupon;

import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CouponTest {

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @DisplayName("쿠폰 생성할 때,")
    @Nested
    class CreateCoupon {

        String name;
        DiscountPolicy discountPolicy;
        Integer discountAmount;
        Double discountRate;
        LocalDateTime issuedAt;
        LocalDateTime expiredAt;
        int quantity;

        @BeforeEach
        void setUp() {
            name = "Test Coupon";
            discountPolicy = DiscountPolicy.FIXED;
            discountAmount = 1000;
            discountRate = null;
            issuedAt = now;
            expiredAt = now.plusDays(1);
            quantity = 10;
        }

        @DisplayName("이름이 비어있으면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenNameIsNullOrBlank() {
            // arrange
            name = null;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("할인 정책이 null이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenDiscountPolicyIsNull() {
            // arrange
            discountPolicy = null;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("정액 할인인 경우, 할인 금액이 null이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenDiscountAmountIsNull() {
            // arrange
            discountAmount = null;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("정액 할인인 경우, 할인 금액이 0 미만이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenDiscountAmountIsNegative() {
            // arrange
            discountAmount = -100;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("할인 비율이 null이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenDiscountRateIsNull() {
            // arrange
            discountPolicy = DiscountPolicy.RATE;
            discountAmount = null;
            discountRate = null;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("할인 비율이 0 미만이거나 1초과면 IllegalArgumentException 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(doubles = {-0.1, 1.1})
        void returnIllegalArgumentException_whenDiscountRateIsOutOfRange() {
            // arrange
            discountPolicy = DiscountPolicy.RATE;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("시작 시간이 종료 시간 이후면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenIssuedAtIsAfterExpiredAt() {
            // arrange
            issuedAt = now.plusDays(2);
            expiredAt = now.plusDays(1);

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("종료 시간이 현재 시간 이전이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenExpiredAtIsBeforeNow() {
            // arrange
            expiredAt = now.minusDays(1);

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("쿠폰 수량이 1 미만이면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenQuantityIsLessThanOne() {
            // arrange
            quantity = 0;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
            });
        }

        @DisplayName("모든 조건을 만족하면 쿠폰이 정상적으로 생성된다.")
        @Test
        void createCouponSuccessfully_whenAllConditionsAreMet() {
            // act
            Coupon coupon = Coupon.create(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);

            // assert
            assertThat(coupon.getName()).isEqualTo(name);
            assertThat(coupon.getDiscountPolicy()).isEqualTo(discountPolicy);
            assertThat(coupon.getDiscountAmount()).isEqualTo(discountAmount);
            assertThat(coupon.getDiscountRate()).isEqualTo(discountRate);
            assertThat(coupon.getIssuedAt()).isEqualTo(issuedAt);
            assertThat(coupon.getExpiredAt()).isEqualTo(expiredAt);
            assertThat(coupon.getQuantity()).isEqualTo(quantity);
        }
    }

    @DisplayName("쿠폰을 발급할 때, ")
    @Nested
    class PublishCoupon {

        Coupon coupon;

        @DisplayName("쿠폰이 활성화 상태가 아니면 IllegalStateException 예외가 발생한다.")
        @Test
        void returnIllegalStateException_whenCouponIsNotActive() {
            // arrange
            coupon = new Coupon("Test Coupon", DiscountPolicy.FIXED, 1000,
                    null, now, now.plusDays(1), 10, CouponStatus.INACTIVE);

            // act & assert
            assertThrows(IllegalStateException.class, coupon::publish);
        }

        @DisplayName("쿠폰 수량이 부족하면 IllegalArgumentException 예외가 발생한다.")
        @Test
        void returnIllegalArgumentException_whenCouponQuantityIsInsufficient() {
            // arrange
            coupon = new Coupon("Test Coupon", DiscountPolicy.FIXED, 1000,
                    null, now, now.plusDays(1), 0, CouponStatus.ACTIVE);

            // act & assert
            assertThrows(IllegalArgumentException.class, coupon::publish);
        }

        @DisplayName("유효 기간이 지났으면 IllegalStateException 예외가 발생한다.")
        @Test
        void returnIllegalStateException_whenCouponIsExpired() {
            // arrange
            coupon = new Coupon("Test Coupon", DiscountPolicy.FIXED, 1000,
                    null, now.minusDays(2), now.minusDays(1), 10);

            // act & assert
            assertThrows(IllegalStateException.class, coupon::publish);
        }

        @DisplayName("쿠폰이 정상적으로 발급되면 쿠폰 개수가 1 감소한다.")
        @Test
        void decreaseCouponQuantity_whenCouponIsPublished() {
            // arrange
            coupon = new Coupon("Test Coupon", DiscountPolicy.FIXED, 1000,
                    null, now, now.plusDays(1), 10, CouponStatus.ACTIVE);

            // act
            coupon.publish();

            // assert
            assertThat(coupon.getQuantity()).isEqualTo(9);
        }
    }
}
