package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    @Override
    public BrandEntity save(BrandEntity brandEntity) {
        return brandJpaRepository.save(brandEntity);
    }

    @Override
    public Optional<BrandEntity> findById(Long brandId) {
        return brandJpaRepository.findById(brandId);
    }
}
