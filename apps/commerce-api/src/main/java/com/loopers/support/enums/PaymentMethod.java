package com.loopers.support.enums;

public enum PaymentMethod {
    CARD, POINT;

    public static PaymentMethod from(String method) {
        for (PaymentMethod pm : values()) {
            if (pm.name().equalsIgnoreCase(method)) {
                return pm;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + method);
    }
}
