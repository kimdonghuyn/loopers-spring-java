package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentInfo;

import java.util.List;
import java.util.Map;

public class PgSimulatorDto {
    public class CardPayment {
        public class Create {
            public record Request(
                    String orderId,
                    String cardType,
                    String cardNo,
                    Long amount,
                    String callbackUrl
            ) {
                public static Request from(
                        PaymentCommand.Card.Payment command,
                        String callbackUrl
                ) {
                    return new Request(
                            command.orderKey(),
                            command.cardType(),
                            command.cardNo(),
                            command.amount(),
                            callbackUrl
                    );
                }
            }

            public record Response(
                    String transactionKey,
                    String status,
                    String reason
            ) {
                public PaymentInfo.Card.Result toPaymentInfo() {
                    return new PaymentInfo.Card.Result(
                            transactionKey,
                            status,
                            reason
                    );
                }
            }
        }

        public class Get {
            public record Request(
                    String transactionKey
            ) {}

            public record Response(
                    String transactionKey,
                    String orderId,
                    String cardType,
                    String cardNo,
                    Long amount,
                    String status,
                    String reason
            ) {
                public PaymentInfo.Card.Get toPaymentInfo() {
                    return new PaymentInfo.Card.Get(
                            transactionKey,
                            orderId,
                            cardType,
                            cardNo,
                            amount,
                            status,
                            reason
                    );
                }
            }
        }

        public static class Order {
            public record Request(
                    String orderId
            ) {}

            public record Response(
                    List<Object> payments
            ) {
                public PaymentInfo.Card.Order toPaymentInfo() {
                    List<PaymentInfo.Card.Result> paymentResults = payments.stream()
                            .map(payment -> {
                                var map = (Map<String, Object>) payment;
                                return new PaymentInfo.Card.Result(
                                        (String) map.get("transactionKey"),
                                        (String) map.get("status"),
                                        (String) map.get("reason")
                                );
                            })
                            .toList();
                    return new PaymentInfo.Card.Order(paymentResults);
                }
            }
        }
    }
}
