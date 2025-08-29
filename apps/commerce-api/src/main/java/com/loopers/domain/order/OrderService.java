package com.loopers.domain.order;

import com.loopers.support.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderInfo order(OrderCommand.Order orderCommand) {
        List<OrderItemEntity> orderItems = orderCommand.orderItems().stream()
                .map(item -> new OrderItemEntity(item.productId(), item.quantity(), item.price()))
                .toList();

        String orderKey = "ORDER-" + UUID.randomUUID().toString().replace("-", "");

        OrderEntity order = new OrderEntity(orderCommand.userId(), orderItems, OrderStatus.PENDING, orderKey);
        OrderEntity savedOrder = orderRepository.save(order);

        return OrderInfo.from(savedOrder);
    }

    public List<OrderInfo> getOrdersByStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        List<OrderEntity> orders = orderRepository.findAllByStatus(orderStatus);
        return orders.stream()
                .map(OrderInfo::from)
                .toList();
    }

    public void updateOrderStatus(OrderCommand.UpdateOrderStatus command) {
        OrderEntity order = orderRepository.findByOrderKey(command.orderKey())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with key: " + command.orderKey()));

        order.updateStatus(OrderStatus.valueOf(command.status()));
        orderRepository.save(order);
    }
}
