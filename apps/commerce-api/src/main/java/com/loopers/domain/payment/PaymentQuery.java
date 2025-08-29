package com.loopers.domain.payment;

public class PaymentQuery {
    public static class Card {
        public record Order(
                String orderKey
        ) {}
    }
}
