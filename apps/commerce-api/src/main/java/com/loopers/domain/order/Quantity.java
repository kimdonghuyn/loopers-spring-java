package com.loopers.domain.order;

import jakarta.persistence.Embeddable;
import lombok.Getter;


@Getter
@Embeddable
public class Quantity {
    private int quantity;

    public Quantity() {}

    public Quantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }

        this.quantity = quantity;
    }
}
