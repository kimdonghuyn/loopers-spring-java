package com.loopers.domain.coupon;

import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    public int applyCoupon(
            final List<Integer> productPrices,
            final CouponStatus couponStatus,
            final LocalDateTime expiredAt,
            final DiscountPolicy discountPolicy,
            final Double discountRate,
            final int discountAmount
    ) {
        if (couponStatus == CouponStatus.INACTIVE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰이 비활성화 상태입니다.");
        }

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "사용기한이 지난 쿠폰입니다.");
        }

        if (productPrices.isEmpty()) {
            return 0;
        }

        int totalPrice = productPrices.stream().mapToInt(Integer::intValue).sum();

        if (discountPolicy == DiscountPolicy.RATE) {
            return (int) (totalPrice * (1 - discountRate));
        } else if (discountPolicy == DiscountPolicy.FIXED) {
            return totalPrice - discountAmount;
        } else {
            throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 할인 정책입니다.");
        }
    }
}
