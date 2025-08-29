package com.loopers.domain.payment;

public interface PaymentGateway {
    PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Card.Payment command);

    PaymentInfo.Card.Get findCardPaymentResult(PaymentCommand.Card.Get command);

    PaymentInfo.Card.Order findCardOrderResult(String orderKey);
}
