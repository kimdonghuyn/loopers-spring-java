package com.loopers.application.order;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.Quantity;
import com.loopers.domain.product.ProductInfo;
import com.loopers.support.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCriteria {
    public record Order(
            String loginId,
            PaymentMethod paymentMethod,
            List<OrderItem> orderItems,
            String cardType,
            String cardNo
    ) {
        public OrderCommand.Order toCommand(Long userId, List<ProductInfo> productInfos, BigDecimal discountedTotalPrice) {
            Map<Long, Integer> productQuantities = new HashMap<>();
            for (OrderItem item : orderItems) {
                productQuantities.put(item.productId(), item.quantity());
            }

            List<OrderCommand.OrderItem> commandItems = new ArrayList<>();
            for (ProductInfo info : productInfos) {
                Quantity quantity = new Quantity(productQuantities.get(info.id()));
                commandItems.add(
                        new OrderCommand.OrderItem(info.id(), quantity, discountedTotalPrice)
                );
            }

            return new OrderCommand.Order(userId, commandItems);
        }
    }
    public record OrderItem(Long productId, int quantity) {}

//    public record Payment(
//            Long orderId,
//            String loginId,
//            int totalPrice
//    ) {
//        public static PaymentCommand.Payment toCommand(Long orderId, String loginId, BigDecimal totalPrice) {
//            return new PaymentCommand.Payment(orderId, loginId, totalPrice);
//        }
//    }
}
