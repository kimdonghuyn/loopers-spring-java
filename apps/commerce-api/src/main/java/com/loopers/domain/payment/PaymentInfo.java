package com.loopers.domain.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInfo {
    public record Get(
            Long id,
            String orderKey,
            String transactionKey,
            String status,
            Long amount
    ) {
        public static Get from(PaymentEntity payment) {
            return new Get(
                    payment.getId(),
                    payment.getOrderKey(),
                    payment.getTransactionKey(),
                    payment.getStatus().name(),
                    payment.getAmount()
            );
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Card {
        public record Result(
                String transactionKey,
                String status,
                String reason
        ) {
            public static Result from(String transactionKey, String status, String reason) {
                return new Result(transactionKey, status, reason);
            }
        }

        public record Get(
                String transactionKey,
                String orderId,
                String cardType,
                String cardNo,
                Long amount,
                String status,
                String reason
        ) {
            public Result toResult() {
                return new Result(transactionKey, status, reason);
            }
        }

        public record Order(
                List<Result> paymentResults
        ) {
        }
    }
}
