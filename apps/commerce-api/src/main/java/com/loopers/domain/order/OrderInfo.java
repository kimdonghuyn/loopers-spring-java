package com.loopers.domain.order;

import com.loopers.support.OrderStatus;

import java.util.List;

public record OrderInfo(
        Long id,
        Long userId,
        OrderStatus status,
        List<OrderItemEntity> orderItems,
        int totalPrice
) {
    public static OrderInfo from(OrderEntity orderEntity) {
        return new OrderInfo(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getStatus(),
                orderEntity.getOrderItems(),
                orderEntity.calculateTotalPrice()
        );
    }
}
