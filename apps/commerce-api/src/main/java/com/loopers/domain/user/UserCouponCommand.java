package com.loopers.domain.user;

public class UserCouponCommand {

    public record GetUserCoupon(
            Long userId
    ) {
    }

    public record UseCoupon(
            Long userCouponId
    ) {
    }
}
