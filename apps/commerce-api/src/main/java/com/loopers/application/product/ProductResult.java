package com.loopers.application.product;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.product.ProductInfo;

import java.math.BigDecimal;

public record ProductResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        int likeCount,
        Long brandId,
        String brandName,
        String brandDescription
) {
    public static ProductResult from(final ProductInfo productInfo, final BrandInfo brandInfo) {
        return new ProductResult(
                productInfo.id(),
                productInfo.name(),
                productInfo.description(),
                productInfo.price(),
                productInfo.stock(),
                productInfo.likeCount(),
                brandInfo.id(),
                brandInfo.name(),
                brandInfo.description()
        );
    }
}
