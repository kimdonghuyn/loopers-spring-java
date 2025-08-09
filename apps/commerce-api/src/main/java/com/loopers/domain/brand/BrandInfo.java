package com.loopers.domain.brand;

public record BrandInfo(
        Long id,
        String name,
        String description
) {
    public static BrandInfo from(final BrandEntity brandEntity) {
        return new BrandInfo(
                brandEntity.getId(),
                brandEntity.getName(),
                brandEntity.getDescription()
        );
    }
}
