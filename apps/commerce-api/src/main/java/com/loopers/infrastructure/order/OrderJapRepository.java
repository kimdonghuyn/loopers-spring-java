package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.support.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderJapRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderKey(String orderKey);

    List<OrderEntity> findAllByStatus(OrderStatus status);
}
