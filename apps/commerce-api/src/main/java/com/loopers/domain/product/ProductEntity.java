package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long brandId;

    public ProductEntity(
            final String name,
            final String description,
            final BigDecimal price,
            final int stock,
            final Long brandId
    ) {
        if (name == null) {
            throw new IllegalArgumentException("상품 이름은 null일 수 없습니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("상품 설명은 null일 수 없습니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("상품 재고는 음수일 수 없습니다.");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("브랜드는 null일 수 없습니다.");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.brandId = brandId;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "추가할 재고 수량은 0보다 커야 합니다.");
        }
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0 || quantity > this.stock) {
            throw new CoreException(ErrorType.BAD_REQUEST, "재고 수량이 부족하거나 잘못된 수량입니다.");
        }
        this.stock -= quantity;
    }
}
