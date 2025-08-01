package com.loopers.domain.product;

import com.loopers.domain.order.Quantity;

public class ProductCommand {
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

    public record Consume(
            Long productId,
            Quantity quantity
    ) {
    }
}
