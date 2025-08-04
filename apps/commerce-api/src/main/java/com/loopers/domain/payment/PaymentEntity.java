package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;

public class PaymentEntity extends BaseEntity {
    private Long userId;
    private int totalPrice;

    public PaymentEntity(final Long userId, final int totalPrice) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }

        if (totalPrice < 0) {
            throw new IllegalArgumentException("totalPrice는 0 이상이어야 합니다.");
        }

        this.userId = userId;
        this.totalPrice = totalPrice;
    }
}
