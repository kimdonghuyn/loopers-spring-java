package com.loopers.domain.product;

import com.loopers.support.SortType;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    ProductEntity save(ProductEntity productEntity);

    Optional<ProductEntity> findById(Long id);

    List<ProductWithLikeCount> findAllOrderBySortType(String sortType);
}
