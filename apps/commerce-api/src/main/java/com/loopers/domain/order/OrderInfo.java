package com.loopers.domain.order;

import com.loopers.support.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderInfo(
        Long id,
        Long userId,
        OrderStatus status,
        List<OrderItemEntity> orderItems,
        BigDecimal totalPrice,
        String orderKey
) {
    public static OrderInfo from(OrderEntity orderEntity) {
        return new OrderInfo(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getStatus(),
                orderEntity.getOrderItems(),
                orderEntity.calculateTotalPrice(),
                orderEntity.getOrderKey()
        );
    }
}
