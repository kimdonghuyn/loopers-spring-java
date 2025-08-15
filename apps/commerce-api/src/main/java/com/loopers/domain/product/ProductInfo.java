package com.loopers.domain.product;

import java.math.BigDecimal;

public record ProductInfo(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        Long brandId,
        int likeCount
) {
    public static ProductInfo from(final ProductWithLikeCount productWithLikeCount) {
        return new ProductInfo(
                productWithLikeCount.product().getId(),
                productWithLikeCount.product().getName(),
                productWithLikeCount.product().getDescription(),
                productWithLikeCount.product().getPrice(),
                productWithLikeCount.product().getStock(),
                productWithLikeCount.product().getBrandId(),
                Math.toIntExact(productWithLikeCount.likeCount())
        );
    }

    public static  ProductInfo from(final ProductEntity product) {
        return new ProductInfo(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getBrandId(),
                product.getLikeCount()
        );
    }
}
