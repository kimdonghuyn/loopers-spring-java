package com.loopers.application.user;

import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.domain.user.UserCouponService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponFacade {

    private final UserCouponService userCouponService;
    private final CouponService couponService;

    public UserCouponResult.CouponWithDetail getUserCoupons(final UserCouponCriteria.GetUserCoupon criteria) {
        UserCouponInfo userCouponInfo = userCouponService.getUserCoupon(
                UserCouponCriteria.GetUserCoupon.toCommand(criteria.userId())
        );

        CouponInfo couponInfo = couponService.getCoupon(userCouponInfo.couponId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "쿠폰 정보를 찾을 수 없습니다."));

        return UserCouponResult.CouponWithDetail.from(userCouponInfo, couponInfo);
    }

    public void useCoupon(final UserCouponCriteria.UseCoupon criteria) {
        userCouponService.useCoupon(
                UserCouponCriteria.UseCoupon.toCommand(criteria.userCouponId())
        );
    }
}
