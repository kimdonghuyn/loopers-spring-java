package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductQuery;
import com.loopers.application.product.ProductResult;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductWithBrand;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductV1Dto {
    public record GetProductsRequest(
            Long brandId,
            Pageable pageable
    ) {
        public static ProductQuery.GetProductsByBrandId toQuery(
                final Long brandId,
                final Pageable pageable
        ) {
            return new ProductQuery.GetProductsByBrandId(
                    brandId,
                    pageable
            );
        }
    }

    public record GetProductsResponse(
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
        public static List<GetProductsResponse> from(final List<ProductWithBrand> productResult) {
            return productResult.stream()
                    .map(productWithBrand -> new GetProductsResponse(
                            productWithBrand.product().getId(),
                            productWithBrand.product().getName(),
                            productWithBrand.product().getDescription(),
                            productWithBrand.product().getPrice(),
                            productWithBrand.product().getStock(),
                            Math.toIntExact(productWithBrand.product().getLikeCount()),
                            productWithBrand.brand().getId(),
                            productWithBrand.brand().getName(),
                            productWithBrand.brand().getDescription()
                    ))
                    .toList();
        }
    }
}
