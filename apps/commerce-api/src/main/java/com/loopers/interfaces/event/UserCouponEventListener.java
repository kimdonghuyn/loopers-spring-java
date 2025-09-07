package com.loopers.interfaces.event;

import com.loopers.application.order.OrderEvent;
import com.loopers.application.user.UserCouponCriteria;
import com.loopers.application.user.UserCouponFacade;
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
public class UserCouponEventListener {

    private final UserCouponFacade userCouponFacade;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(final OrderEvent event) {
        userCouponFacade.useCoupon(new UserCouponCriteria.UseCoupon(event.userCouponInfo().id()));
    }
}
