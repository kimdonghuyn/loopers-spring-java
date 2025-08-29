package com.loopers.domain.payment;

import com.loopers.support.enums.PaymentMethod;

public class PaymentCommand {
    public record Payment(
            String loginId,
            String orderKey,
            Long amount,
            PaymentMethod paymentMethod,
            String cardType,
            String cardNo
    )  {}

    public record CreatePayment(
            String transactionKey,
            String orderId,
            String cardType,
            String cardNo,
            Long amount,
            String status,
            String reason
    ) {}

    public class Card {
        public record Payment(
                String loginId,
                String orderKey,
                Long amount,
                String cardType,
                String cardNo
        ) {}
        public record Get(
                String loginId,
                String transactionKey
        ) {}
    }

    public class Point {
        public record Payment(
                String loginId,
                Long amount
        ) {}
    }
}
