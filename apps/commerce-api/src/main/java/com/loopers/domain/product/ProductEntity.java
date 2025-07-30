package com.loopers.domain.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_brand_id")
    private BrandEntity brand;

    public ProductEntity(
            final String name,
            final String description,
            final int price,
            final int stock,
            final BrandEntity brand
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
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
