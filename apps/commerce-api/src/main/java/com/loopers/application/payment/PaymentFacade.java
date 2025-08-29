package com.loopers.application.payment;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PaymentFacade {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;

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

        orderService.updateOrderStatus(
                new OrderCommand.UpdateOrderStatus(
                        cardPaymentResult.orderId(),
                        paymentCriteria.status()
                )
        );
    }
}
