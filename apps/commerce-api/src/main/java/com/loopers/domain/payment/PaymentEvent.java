package com.loopers.domain.payment;

public record PaymentEvent(
        String orderId, String status
) {}
