package com.loopers.domain.coupon;

import com.loopers.support.enums.CouponStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

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

    @Transactional
    public int calculateDiscountPrice(final List<Integer> productPrices, final Long couponId) {
        return couponRepository.findById(couponId)
                .map(coupon -> {
                    if (coupon.getExpiredAt().isBefore(now())) {
                        throw new CoreException(ErrorType.BAD_REQUEST, "사용기한이 지난 쿠폰입니다.");
                    }
                    if (coupon.getStatus() == CouponStatus.INACTIVE) {
                        throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰이 활성화되지 않았습니다.");
                    }
                    return switch (coupon.getDiscountPolicy()) {
                        case FIXED -> productPrices.stream()
                                .mapToInt(price -> Math.max(price - coupon.getDiscountAmount(), 0))
                                .sum();
                        case RATE -> productPrices.stream()
                                .mapToInt(price -> {
                                    int off = (int) Math.round(price * (coupon.getDiscountRate() / 100.0));
                                    return Math.max(price - off, 0);
                                })
                                .sum();
                        default -> throw new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 할인 정책입니다.");
                    };
                })
                .orElse(0);
    }
}
