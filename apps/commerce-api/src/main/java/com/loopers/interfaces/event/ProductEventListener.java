package com.loopers.interfaces.event;

import com.loopers.application.like.LikeEvent;
import com.loopers.application.product.ProductApplicationService;
import com.loopers.application.product.ProductFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductEventListener {

    private final ProductApplicationService productApplicationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeEvent(final LikeEvent event) {
        productApplicationService.updateLikeCount(event.productId(), event.isLike());
    }
}
