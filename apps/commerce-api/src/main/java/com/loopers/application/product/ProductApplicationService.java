package com.loopers.application.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductApplicationService {

    private final ProductRepository productRepository;

    @Transactional
    public void updateLikeCount(Long productId, boolean isLike) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        if (isLike) {
            product.increaseLikeCount();
        } else {
            product.decreaseLikeCount();
        }

        productRepository.save(product);
    }
}
