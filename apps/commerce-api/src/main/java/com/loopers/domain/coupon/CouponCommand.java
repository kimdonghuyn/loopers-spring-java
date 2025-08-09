package com.loopers.domain.coupon;

import com.loopers.support.enums.DiscountPolicy;

import java.time.LocalDateTime;

public class CouponCommand {
    public record  Create(
            String name,
            DiscountPolicy discountPolicy,
            Integer discountAmount,
            Double discountRate,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt,
            int quantity
    ) {}
}
