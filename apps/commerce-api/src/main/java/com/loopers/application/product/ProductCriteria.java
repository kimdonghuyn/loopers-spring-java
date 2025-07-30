package com.loopers.application.product;

import com.loopers.domain.product.ProductCommand;

public class ProductCriteria {
    public record Create(
            String name,
            String description,
            int price,
            int stock,
            Long brandId
    ) {
        public ProductCommand.Create toCommand() {
            return new ProductCommand.Create(name, description, price, stock, brandId);
        }
    }
}
