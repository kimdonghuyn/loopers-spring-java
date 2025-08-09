package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJapRepository orderJapRepository;

    @Override
    public
    OrderEntity save(OrderEntity order) {
        return orderJapRepository.save(order);
    }
}
