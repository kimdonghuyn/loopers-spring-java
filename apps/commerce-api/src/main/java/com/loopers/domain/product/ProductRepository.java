package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    ProductEntity save(ProductEntity productEntity);

    ProductWithLikeCount findDetailById(Long id);

    List<ProductWithLikeCount> findAllOrderBySortType(String sortType);

    List<ProductWithLikeCount> findAllById(List<Long> productIds);

    Optional<ProductEntity> findById(Long id);

    Optional<ProductEntity> findByIdForUpdate(Long id);

    List<ProductWithLikeCount> findLikedProductsByUserId(Long userId);
}
