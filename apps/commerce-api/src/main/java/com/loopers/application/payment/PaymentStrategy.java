package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentCommand;

public interface PaymentStrategy {
    void processPayment(PaymentCommand.Payment payment);
}
