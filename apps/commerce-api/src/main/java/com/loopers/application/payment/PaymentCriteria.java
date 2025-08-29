package com.loopers.application.payment;

public class PaymentCriteria {
    public record createPayment(
            String loginId,
            String transactionKey,
            String status,
            String reason
    ) {
        public static PaymentCriteria.createPayment toCriteria(
                String loginId,
                String transactionKey,
                String status,
                String reason
        ) {
            return new PaymentCriteria.createPayment(
                    loginId,
                    transactionKey,
                    status,
                    reason
            );
        }
    }
}
