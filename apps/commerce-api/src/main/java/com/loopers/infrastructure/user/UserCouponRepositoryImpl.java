package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserCoupon;
import com.loopers.domain.user.UserCouponRepository;
import com.loopers.support.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public UserCoupon findByUserId(Long userId) {
        return userCouponJpaRepository.findByUserId(userId);
    }

    @Override
    public void updateStatus(Long userCouponId, CouponStatus status) {
        userCouponJpaRepository.updateStatus(userCouponId, status);
    }
}
