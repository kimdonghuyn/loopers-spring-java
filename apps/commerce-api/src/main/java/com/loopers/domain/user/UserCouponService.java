package com.loopers.domain.user;

import com.loopers.support.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public UserCouponInfo getUserCoupon(final UserCouponCommand.GetUserCoupon command) {
        return UserCouponInfo.from(userCouponRepository.findByUserId(command.userId()));
    }

    @Transactional
    public void useCoupon(final UserCouponCommand.UseCoupon command) {
        userCouponRepository.updateStatus(command.userCouponId(), CouponStatus.INACTIVE);
    }
}
