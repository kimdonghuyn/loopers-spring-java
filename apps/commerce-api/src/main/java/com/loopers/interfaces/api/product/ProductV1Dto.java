package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductQuery;
import com.loopers.application.product.ProductResult;
import com.loopers.domain.product.ProductEntity;
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
            Long likeCount,
            Long brandId,
            String brandName,
            String brandDescription
    ) {
        public static List<GetProductsResponse> from(final List<Optional<ProductResult>> productResult) {
            return productResult.stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(product -> new GetProductsResponse(
                            product.id(),
                            product.name(),
                            product.description(),
                            product.price(),
                            product.stock(),
                            product.likeCount(),
                            product.brandId(),
                            product.brandName(),
                            product.brandDescription()
                    ))
                    .toList();
        }
    }
}
