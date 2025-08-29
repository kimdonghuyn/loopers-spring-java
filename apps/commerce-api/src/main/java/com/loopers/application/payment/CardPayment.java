package com.loopers.application.payment;

import com.loopers.domain.payment.*;
import org.springframework.stereotype.Component;

@Component("CARD")
public class CardPayment implements PaymentStrategy {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public CardPayment(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void processPayment(PaymentCommand.Payment payment) {
        PaymentInfo.Card.Result result = paymentService.requestCardPayment(payment);
    }
}
