package com.loopers.domain.product;

public record ProductInfo(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        Long brandId,
        Long likeCount
) {
    public static ProductInfo from(final ProductWithLikeCount productWithLikeCount) {
        return new ProductInfo(
                productWithLikeCount.product().getId(),
                productWithLikeCount.product().getName(),
                productWithLikeCount.product().getDescription(),
                productWithLikeCount.product().getPrice(),
                productWithLikeCount.product().getStock(),
                productWithLikeCount.product().getBrandId(),
                productWithLikeCount.likeCount()
        );
    }
}
