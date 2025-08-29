package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.support.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {
    private Long userId;
    private Long couponId;
    @Enumerated(EnumType.STRING)
    private CouponStatus status = CouponStatus.ACTIVE;
    private LocalDateTime usedAt;

    public UserCoupon(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    public static UserCoupon create(Long userId, Long couponId) {
        if (userId == null || userId <= 0L) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }

        if (couponId == null || couponId <= 0L) {
            throw new IllegalArgumentException("쿠폰 ID는 null일 수 없습니다.");
        }

        return  new UserCoupon(userId, couponId);
    }

    public void useCoupon() {
        if (this.status == CouponStatus.INACTIVE) {
            throw new IllegalStateException("사용할 수 없는 쿠폰입니다.");
        }

        this.status = CouponStatus.INACTIVE;
        this.usedAt = LocalDateTime.now();
    }
}
