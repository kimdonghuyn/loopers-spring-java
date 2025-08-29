package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.support.enums.CouponStatus;
import com.loopers.support.enums.DiscountPolicy;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {
    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountPolicy discountPolicy;

    private Integer discountAmount;

    private Double discountRate;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    private int quantity;

    @Enumerated(value = STRING)
    private CouponStatus status = CouponStatus.ACTIVE;

    @Version
    private Long version;

    public Coupon(
            String name, DiscountPolicy discountPolicy, Integer discountAmount,
            Double discountRate, LocalDateTime issuedAt, LocalDateTime expiredAt, int quantity
    ) {
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
        this.quantity = quantity;
    }

    public Coupon(
            String name, DiscountPolicy discountPolicy, Integer discountAmount,
            Double discountRate, LocalDateTime issuedAt, LocalDateTime expiredAt, int quantity, CouponStatus status
    ) {
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.quantity = quantity;
        this.status = status;
    }

    public static Coupon create(
            String name, DiscountPolicy discountPolicy, Integer discountAmount,
            Double discountRate, LocalDateTime issuedAt, LocalDateTime expiredAt, int quantity
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("쿠폰 이름은 필수입니다.");
        }

//        if (discountPolicy == null) {
//            throw new IllegalArgumentException("할인 정책은 필수입니다.");
//        }

        if (discountPolicy == DiscountPolicy.FIXED) {
            if (discountAmount == null || discountAmount < 0) {
                throw new IllegalArgumentException("정액 할인은 0 이상의 discountAmount가 필요합니다.");
            }
        }

        if (discountPolicy == DiscountPolicy.RATE) {
            if (discountRate == null || discountRate < 0 || discountRate > 1) {
                throw new IllegalArgumentException("정률 할인은 0 이상 1 이하의 discountRate가 필요합니다.");
            }
        }

        if (issuedAt == null || issuedAt.isAfter(expiredAt)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간 이전이어야 합니다.");
        }

//        if (expiredAt == null || expiredAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("유효 기간은 현재 시간 이후여야 합니다.");
//        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("쿠폰 수량은 1 이상이어야 합니다.");
        }

        return new Coupon(name, discountPolicy, discountAmount, discountRate, issuedAt, expiredAt, quantity);
    }

    public void publish() {

        if (this.status == CouponStatus.INACTIVE) {
            throw new IllegalStateException("쿠폰은 활성화 상태여야 합니다.");
        }
        if (this.quantity <= 0) {
            throw new IllegalArgumentException("쿠폰 수량이 부족합니다.");
        }
        if (this.expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("쿠폰은 유효 기간이 지나면 발급할 수 없습니다.");
        }

        this.quantity--;
    }

    public int calculateDiscountPrice(int price) {
        if (this.discountPolicy == DiscountPolicy.FIXED) {
            return this.discountAmount;
        } else if (this.discountPolicy == DiscountPolicy.RATE) {
            return (int) (price * this.discountRate);
        } else {
            throw new IllegalArgumentException("유효하지 않은 할인 정책입니다.");
        }
    }

    public void useOnceOrThrow(LocalDateTime now) {
        if (this.status == CouponStatus.INACTIVE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰이 활성화 상태가 아닙니다.");
        }
        this.status = CouponStatus.INACTIVE;
    }
}
