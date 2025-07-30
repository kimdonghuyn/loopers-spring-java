package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductWithLikeCount;
import com.loopers.support.SortType;
import lombok.RequiredArgsConstructor;
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
    public Optional<ProductEntity> findById(Long id) {
        return productJpaRepository.findById(id);
    }
}
