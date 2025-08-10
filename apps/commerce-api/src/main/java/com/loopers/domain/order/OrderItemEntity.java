package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderItemEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_order_id")
    private OrderEntity order;

    @Column(name = "ref_product_id", nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    private BigDecimal price;

    public OrderItemEntity(Long productId, Quantity quantity, BigDecimal price) {
        if (productId == null) {
            throw new IllegalArgumentException("productId는 null일 수 없습니다.");
        }

        if (quantity == null || quantity.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity는 null이거나 0 이하일 수 없습니다.");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price는 0 이하일 수 없습니다.");
        }

        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal calculateTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity.getQuantity()));
    }
}
