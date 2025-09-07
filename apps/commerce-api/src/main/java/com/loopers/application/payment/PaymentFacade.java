package com.loopers.application.payment;

import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PaymentFacade {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final ApplicationEventPublisher eventPublisher;

    public void createPayment(PaymentCriteria.createPayment paymentCriteria) {
        PaymentInfo.Card.Get cardPaymentResult = paymentGateway.findCardPaymentResult(
                new PaymentCommand.Card.Get(
                        paymentCriteria.loginId(),
                        paymentCriteria.transactionKey()
                )
        );

        paymentService.createPayment(
                new PaymentCommand.CreatePayment(
                        cardPaymentResult.transactionKey(),
                        cardPaymentResult.orderId(),
                        cardPaymentResult.cardType(),
                        cardPaymentResult.cardNo(),
                        cardPaymentResult.amount(),
                        paymentCriteria.status(),
                        paymentCriteria.reason()
                )
        );

        // 이벤트 발행 ( 주문 상태 변경 )
        eventPublisher.publishEvent(new PaymentEvent(cardPaymentResult.orderId(), paymentCriteria.status()));
    }
}
