package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brand")
@Getter
@NoArgsConstructor
public class BrandEntity extends BaseEntity {
    private String name;
    private String description;

    public BrandEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
