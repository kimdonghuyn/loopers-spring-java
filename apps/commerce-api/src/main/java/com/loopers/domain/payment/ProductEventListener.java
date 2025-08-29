package com.loopers.domain.payment;

import com.loopers.application.like.LikeEvent;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductEventListener {

    private final ProductService productService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeEvent(final LikeEvent event) {
        productService.updateLikeCount(event.productId(), event.isLike());
    }
}
