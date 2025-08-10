package com.loopers.domain.user;

import com.loopers.support.enums.CouponStatus;

import java.time.LocalDateTime;

public record UserCouponInfo(
        Long id,
        Long userId,
        Long couponId,
        LocalDateTime usedAt,
        CouponStatus status
) {
    public static UserCouponInfo from(final UserCoupon userCoupon) {
        return new UserCouponInfo(
                userCoupon.getId(),
                userCoupon.getUserId(),
                userCoupon.getCouponId(),
                userCoupon.getUsedAt(),
                userCoupon.getStatus()
        );
    }
}
