package com.loopers.application.order;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItemEntity;
import com.loopers.support.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResult(
        Long id,
        Long userId,
        OrderStatus status,
        List<OrderItemEntity> orderItems,
        BigDecimal totalPrice
) {
    public static OrderResult from(OrderInfo orderInfo) {
        return new OrderResult(
                orderInfo.id(),
                orderInfo.userId(),
                orderInfo.status(),
                orderInfo.orderItems(),
                orderInfo.totalPrice()
        );
    }
}
