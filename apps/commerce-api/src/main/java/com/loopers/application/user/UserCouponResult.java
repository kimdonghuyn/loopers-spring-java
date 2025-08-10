package com.loopers.application.user;

import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.support.enums.CouponStatus;

import java.time.LocalDateTime;

public record UserCouponResult() {
    public record Coupon(
            Long userId,
            Long couponId,
            LocalDateTime usedAt,
            CouponStatus status
    ) {
        public static UserCouponResult.Coupon from(final UserCouponInfo coupon) {
            return new UserCouponResult.Coupon(
                    coupon.userId(),
                    coupon.couponId(),
                    coupon.usedAt(),
                    coupon.status()
            );
        }
    }

    public record CouponWithDetail(
            Long userCouponId,
            Long userId,
            Long couponId,
            CouponStatus status,
            LocalDateTime usedAt,
            CouponInfo coupon
    ) {
        public static CouponWithDetail from(UserCouponInfo userCouponInfo, CouponInfo couponInfo) {
            return new CouponWithDetail(
                    userCouponInfo.id(),
                    userCouponInfo.userId(),
                    userCouponInfo.couponId(),
                    userCouponInfo.status(),
                    userCouponInfo.usedAt(),
                    couponInfo
            );
        }
    }
}
