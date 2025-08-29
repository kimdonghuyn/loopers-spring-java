package com.loopers.interfaces.api.payment;


public class PaymentV1Dto {
    public class PG {
        public record CallbackRequest(
                String transactionKey,
                String status,
                String reason
        ) {}
    }
}
