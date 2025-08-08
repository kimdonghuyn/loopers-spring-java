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

    public int applyCoupon(Long couponId, Long userId, List<Integer> productPrices) {

        if (couponId == null) {
            return 0;
        }
        // 쿠폰 적용 로직 구현
        // 예시로 쿠폰 ID에 따라 10% 할인 적용
        if (couponId == 1) {
            return productPrices.stream()
                    .mapToInt(price -> price - (price * 10 / 100))
                    .sum();
        }
        // 다른 쿠폰 ID에 대한 로직 추가 가능
        return productPrices.stream().mapToInt(Integer::intValue).sum();
    }
}
