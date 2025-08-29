package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {
    @Column(name = "ref_user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "order_key", nullable = false, unique = true)
    private String orderKey;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public OrderEntity(Long userId, List<OrderItemEntity> orderItems, OrderStatus status, String orderKey) {
        if(userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }

        if(orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 null이거나 비어있을 수 없습니다.");
        }

        if(status == null) {
            throw new IllegalArgumentException("주문 상태는 null일 수 없습니다.");
        }

        if(orderKey == null || orderKey.isEmpty()) {
            throw new IllegalArgumentException("orderKey는 null이거나 비어있을 수 없습니다.");
        }

        this.userId = userId;
        this.orderItems = orderItems;
        this.status = status;
        this.orderKey = orderKey;
    }

    public BigDecimal calculateTotalPrice() {
        return orderItems.stream()
                .map(OrderItemEntity::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("새로운 상태는 null일 수 없습니다.");
        }
        this.status = newStatus;
    }
}
