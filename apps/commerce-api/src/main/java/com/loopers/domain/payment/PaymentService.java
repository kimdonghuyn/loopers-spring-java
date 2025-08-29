package com.loopers.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    public PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Payment command) {
        PaymentInfo.Card.Result result = paymentGateway.requestCardPayment(
                new PaymentCommand.Card.Payment(
                        command.loginId(),
                        command.orderKey(),
                        command.amount(),
                        command.cardType(),
                        command.cardNo()
                )
        );

        return result;
    }

    @Transactional
    public void createPayment(PaymentCommand.CreatePayment command) {
        paymentRepository.save(PaymentEntity.create(command));
    }

    public PaymentInfo.Card.Order findCardPaymentResult(String orderKey) {
        return paymentGateway.findCardOrderResult(orderKey);
    }
}
