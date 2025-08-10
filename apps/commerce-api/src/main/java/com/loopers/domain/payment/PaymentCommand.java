package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Payment(
            Long userId,
            BigDecimal totalPrice
    ) {}
}
