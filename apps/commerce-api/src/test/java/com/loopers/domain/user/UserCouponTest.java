package com.loopers.domain.user;

import com.loopers.support.enums.CouponStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserCouponTest {

    @DisplayName("쿠폰을 사용할 때,")
    @Nested
    class UseCoupon {

        @DisplayName("이미 사용한 쿠폰을 사용하면 IllegalStateException 예외가 발생한다.")
        @Test
        void cannotUseCouponThatIsAlreadyUsed() {
            // arrange
            UserCoupon userCoupon = UserCoupon.create(1L, 1L);

            // act
            userCoupon.useCoupon();

            // assert
            assertThrows(IllegalStateException.class, userCoupon::useCoupon);
        }

        @DisplayName("쿠폰을 사용하면 비활성화 상태로 변경된다.")
        @Test
        void useCouponChangesStatusToUsed() {
            // arrange
            UserCoupon userCoupon = UserCoupon.create(1L, 1L);

            // act
            userCoupon.useCoupon();

            // assert
            assertAll(
                    () -> assertThat(userCoupon.getStatus()).isEqualTo(CouponStatus.INACTIVE),
                    () -> assertThat(userCoupon.getUsedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS))
            );
        }
    }
}
