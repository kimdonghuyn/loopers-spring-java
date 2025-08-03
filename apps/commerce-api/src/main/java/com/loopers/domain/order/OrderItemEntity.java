package com.loopers.domain.order;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_order_id")
    private OrderEntity order;

    @Column(name = "ref_product_id", nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    private int price;
    private LocalDateTime createdAt;

    public OrderItemEntity() {}

    public OrderItemEntity(Long productId, Quantity quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int calculateTotalPrice() {
        return this.quantity.getQuantity() * this.price;
    }
}
