package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJapRepository extends JpaRepository<OrderEntity, Long> {
}
