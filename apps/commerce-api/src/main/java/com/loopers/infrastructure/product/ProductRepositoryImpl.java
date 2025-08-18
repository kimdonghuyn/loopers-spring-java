package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductWithBrand;
import com.loopers.domain.product.ProductWithLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productJpaRepository.save(productEntity);
    }

    @Override
    public List<ProductWithLikeCount> findAllOrderBySortType(String sortType) {
        return productJpaRepository.findAllOrderBySortType(sortType);
    }

    @Override
    public List<ProductWithBrand> findAllByBrandId(Long brandId, Pageable pageable) {
        return productJpaRepository.findAllByBrandId(brandId, pageable);
    }

    @Override
    public ProductWithLikeCount findDetailById(Long id) {
        return productJpaRepository.findProductById(id);
    }

    @Override
    public List<ProductWithLikeCount> findAllById(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Optional<ProductEntity> findByIdForUpdate(Long id) {
        return productJpaRepository.findByIdForUpdate(id);
    }

    @Override
    public List<ProductWithLikeCount> findLikedProductsByUserId(Long userId) {
        return productJpaRepository.findLikedProductsByUserId(userId);
    }
}
