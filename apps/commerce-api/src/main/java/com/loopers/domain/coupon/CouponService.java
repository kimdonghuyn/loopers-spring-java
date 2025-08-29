package com.loopers.domain.coupon;

import com.loopers.domain.user.UserCoupon;
import com.loopers.support.enums.CouponStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public BigDecimal calculateDiscountPrice(final List<BigDecimal> productPrices, final Long couponId) throws ObjectOptimisticLockingFailureException, OptimisticLockException {
        final Coupon coupon = couponRepository.findByIdForUpdate(couponId)
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "쿠폰이 존재하지 않습니다."));

        if (coupon.getExpiredAt().isBefore(now())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "사용기한이 지난 쿠폰입니다.");
        }

        BigDecimal discounted = switch (coupon.getDiscountPolicy()) {
            case FIXED ->
            productPrices.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .subtract(BigDecimal.valueOf(coupon.getDiscountAmount()))
                    .max(BigDecimal.ZERO);
            case RATE ->
            productPrices.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .multiply(BigDecimal.valueOf(1 - coupon.getDiscountRate()))
                    .max(BigDecimal.ZERO);

            default -> throw new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 할인 정책입니다.");
        };

        return discounted;
    }

}
