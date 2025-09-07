package com.loopers.interfaces.event;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PaymentEventListener {

    private final OrderService orderService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateOrderStatus(final PaymentEvent event) {
        orderService.updateOrderStatus(
                new OrderCommand.UpdateOrderStatus(
                        event.orderId(),
                        event.status()
                )
        );
    }
}
