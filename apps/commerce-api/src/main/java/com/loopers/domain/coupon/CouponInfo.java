package com.loopers.domain.coupon;

import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;

import java.time.LocalDateTime;

public record CouponInfo(
        Long id,
        String name,
        DiscountPolicy discountPolicy,
        Integer discountAmount,
        Double discountRate,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt,
        int quantity,
        CouponStatus status
) {
    public static CouponInfo from(final Coupon coupon) {
        return new CouponInfo(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountPolicy(),
                coupon.getDiscountAmount(),
                coupon.getDiscountRate(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt(),
                coupon.getQuantity(),
                coupon.getStatus()
        );
    }
}
