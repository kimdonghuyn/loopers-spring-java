package com.loopers.domain.order;

import com.loopers.support.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    OrderEntity save(OrderEntity order);

    Optional<OrderEntity> findByOrderKey(String orderKey);

    List<OrderEntity> findAllByStatus(OrderStatus status);
}
