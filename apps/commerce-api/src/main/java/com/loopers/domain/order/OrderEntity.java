package com.loopers.domain.order;

import com.loopers.support.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public OrderEntity() {}

    public OrderEntity(Long userId, List<OrderItemEntity> orderItems, OrderStatus status) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.status = status;
    }

    public int calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItemEntity::calculateTotalPrice)
                .sum();
    }
}
