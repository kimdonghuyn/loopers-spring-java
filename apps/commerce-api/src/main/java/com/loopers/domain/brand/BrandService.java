package com.loopers.domain.brand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Optional<BrandInfo> getBrandInfo(Long brandId) {
        return brandRepository.findById(brandId)
                .map(BrandInfo::from);
    }
}
