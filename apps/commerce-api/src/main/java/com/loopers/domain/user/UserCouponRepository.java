package com.loopers.domain.user;

import com.loopers.support.enums.CouponStatus;

public interface UserCouponRepository {
    UserCoupon findByUserId(Long userId);

    void updateStatus(Long userCouponId, CouponStatus status);
}
