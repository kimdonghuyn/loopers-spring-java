package com.loopers.application.user;

import com.loopers.domain.user.UserCouponCommand;

public class UserCouponCriteria {
    public record GetUserCoupon(Long userId) {
        public static UserCouponCommand.GetUserCoupon toCommand(final Long userId) {
            return new UserCouponCommand.GetUserCoupon(userId);
        }
    }

    public record UseCoupon(Long userCouponId) {
        public static UserCouponCommand.UseCoupon toCommand(final Long userCouponId) {
            return new UserCouponCommand.UseCoupon(userCouponId);
        }
    }
}
