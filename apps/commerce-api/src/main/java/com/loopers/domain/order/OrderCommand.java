package com.loopers.domain.order;

import java.util.List;

public class OrderCommand{

    public record Order(Long userId, List<OrderItem> orderItems) {
    }

    public record OrderItem(Long productId, Quantity quantity, int price) {}
}
