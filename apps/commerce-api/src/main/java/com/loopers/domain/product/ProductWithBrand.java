package com.loopers.domain.product;

import com.loopers.domain.brand.BrandEntity;

public record ProductWithBrand(
        ProductEntity product,
        BrandEntity brand
) {
}
