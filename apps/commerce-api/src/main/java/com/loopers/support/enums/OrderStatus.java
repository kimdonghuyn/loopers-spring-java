package com.loopers.support.enums;

public enum OrderStatus {
    PENDING("주문 대기"),
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
