package com.loopers.domain.payment;

public class PaymentCommand {
    public record Payment(
            Long userId,
            int totalPrice
    ) {}
}
