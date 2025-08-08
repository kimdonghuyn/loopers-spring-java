package com.loopers.application.order;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.Quantity;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.product.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCriteria {
    public record Order(
            Long userId,
            String loginId,
            List<OrderItem> orderItems
    ) {
        public OrderCommand.Order toCommand(List<ProductInfo> productInfos) {
            Map<Long, Integer> productQuantities = new HashMap<>();
            for (OrderItem item : orderItems) {
                productQuantities.put(item.productId(), item.quantity());
            }

            List<OrderCommand.OrderItem> commandItems = new ArrayList<>();
            for (ProductInfo info : productInfos) {
                Quantity quantity = new Quantity(productQuantities.get(info.id()));
                commandItems.add(
                        new OrderCommand.OrderItem(info.id(), quantity, info.price())
                );
            }

            return new OrderCommand.Order(userId, commandItems);
        }
    }
    public record OrderItem(Long productId, int quantity) {}

    public record Payment(
            Long userId,
            int totalPrice
    ) {
        public static PaymentCommand.Payment toCommand(Long userId, int totalPrice) {
            return new PaymentCommand.Payment(userId, totalPrice);
        }
    }
}
