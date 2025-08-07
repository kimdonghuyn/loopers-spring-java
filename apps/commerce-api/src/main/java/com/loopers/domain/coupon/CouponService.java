package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon createCoupon(CouponCommand.Create command) {
        return couponRepository.save(
                Coupon.create(
                        command.name(),
                        command.discountPolicy(),
                        command.discountAmount(),
                        command.discountRate(),
                        command.issuedAt(),
                        command.expiredAt(),
                        command.quantity()
                ));
    }

    @Transactional
    public void publishCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "쿠폰이 존재하지 않습니다."));

        coupon.publish();
        couponRepository.save(coupon);
    }

    public Optional<CouponInfo> getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .map(CouponInfo::from);
    }
}
