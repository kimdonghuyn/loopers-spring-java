package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brand")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BrandEntity extends BaseEntity {
    private String name;
    private String description;

    public BrandEntity(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
