package com.loopers.domain.product;

public record ProductInfo(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        Long likeCount,
        String brandName,
        String brandDescription
) {
    public static ProductInfo from(final ProductWithLikeCount productWithLikeCount) {
        return new ProductInfo(
                productWithLikeCount.product().getId(),
                productWithLikeCount.product().getName(),
                productWithLikeCount.product().getDescription(),
                productWithLikeCount.product().getPrice(),
                productWithLikeCount.product().getStock(),
                productWithLikeCount.likeCount(),
                productWithLikeCount.product().getBrand().getName(),
                productWithLikeCount.product().getBrand().getDescription()
        );
    }
}
