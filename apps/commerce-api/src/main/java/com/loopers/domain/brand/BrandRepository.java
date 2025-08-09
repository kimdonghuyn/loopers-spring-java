package com.loopers.domain.brand;

import java.util.Optional;

public interface BrandRepository {
    BrandEntity save(BrandEntity brandEntity);

    Optional<BrandEntity> findById(Long brandId);
}
