package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderRepository;
import com.loopers.support.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJapRepository orderJapRepository;

    @Override
    public
    OrderEntity save(OrderEntity order) {
        return orderJapRepository.save(order);
    }

    @Override
    public Optional<OrderEntity> findByOrderKey(String orderKey) {
        return orderJapRepository.findByOrderKey(orderKey);
    }

    @Override
    public List<OrderEntity> findAllByStatus(OrderStatus status) {
        return orderJapRepository.findAllByStatus(status);
    }


}
