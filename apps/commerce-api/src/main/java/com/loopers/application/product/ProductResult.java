package com.loopers.application.product;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.user.UserInfo;
import com.loopers.support.Gender;

public record ProductResult(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        Long likeCount,
        String brandName,
        String brandDescription
) {
    public static ProductResult from(final ProductInfo productInfo) {
        return new ProductResult(
                productInfo.id(),
                productInfo.name(),
                productInfo.description(),
                productInfo.price(),
                productInfo.stock(),
                productInfo.likeCount(),
                productInfo.brandName(),
                productInfo.brandDescription()
        );
    }
}
