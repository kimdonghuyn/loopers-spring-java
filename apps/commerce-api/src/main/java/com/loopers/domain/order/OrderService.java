package com.loopers.domain.order;

import com.loopers.support.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderInfo order(OrderCommand.Order orderCommand) {
        List<OrderItemEntity> orderItems = orderCommand.orderItems().stream()
                .map(item -> new OrderItemEntity(item.productId(), item.quantity(), item.price()))
                .toList();

        OrderEntity order = new OrderEntity(orderCommand.userId(), orderItems, OrderStatus.COMPLETED);
        OrderEntity savedOrder = orderRepository.save(order);

        return OrderInfo.from(savedOrder);
    }
}
