package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderResult;
import com.loopers.support.enums.PaymentMethod;

import java.util.List;

public class OrderV1Dto {

    public record OrderRequest(
            List<OrderCriteria.OrderItem> orderItems,
            String paymentMethod,
            String cardType,
            String cardNo
    ) {
        public OrderCriteria.Order toCriteria(String loginId) {
            return new OrderCriteria.Order(
                    loginId,
                    PaymentMethod.valueOf(paymentMethod),
                    orderItems,
                    cardType,
                    cardNo
            );
        }
    }

    public record OrderResponse(
            Long orderId,
            Long amount,
            String status
    ) {
        public static OrderResponse from(OrderResult result) {
            return new OrderResponse(
                    result.id(),
                    result.totalPrice().longValue(),
                    result.status().name()
            );
        }
    }
}
