package com.loopers.application.user;

import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.domain.user.UserCouponService;
import com.loopers.support.enums.CouponStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void useCoupon(final UserCouponCriteria.UseCoupon criteria) {
        UserCouponInfo userCouponInfo = userCouponService.getUserCoupon(
                UserCouponCriteria.GetUserCoupon.toCommand(criteria.userCouponId()
                ));

        if (userCouponInfo.status().equals(CouponStatus.ACTIVE)) {
            userCouponService.useCoupon(
                    UserCouponCriteria.UseCoupon.toCommand(criteria.userCouponId())
            );
        } else if (userCouponInfo.status().equals(CouponStatus.INACTIVE)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 만료된 쿠폰입니다.");
        }
    }
}
