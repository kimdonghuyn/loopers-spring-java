package com.loopers.application.product;

import org.springframework.data.domain.Pageable;

public class ProductQuery {
    public record GetProductsByBrandId(
            Long brandId,
            Pageable pageable
    ) {
    }
}
