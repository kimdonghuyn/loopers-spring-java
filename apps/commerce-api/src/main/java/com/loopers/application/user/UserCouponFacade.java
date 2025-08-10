package com.loopers.application.user;

import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.domain.user.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCouponFacade {

    private final UserCouponService userCouponService;
    private final CouponService couponService;

    public UserCouponResult.CouponWithDetail getUserCoupon(final UserCouponCriteria.GetUserCoupon criteria) {
        UserCouponInfo userCouponInfo = userCouponService.getUserCoupon(
                UserCouponCriteria.GetUserCoupon.toCommand(criteria.userId())
        );

        Optional<CouponInfo> couponInfo = couponService.getCoupon(userCouponInfo.couponId());

        return UserCouponResult.CouponWithDetail.from(userCouponInfo, couponInfo.orElse(null));
    }

    public void useCoupon(final UserCouponCriteria.UseCoupon criteria) {
        userCouponService.useCoupon(
                UserCouponCriteria.UseCoupon.toCommand(criteria.userCouponId())
        );
    }
}
