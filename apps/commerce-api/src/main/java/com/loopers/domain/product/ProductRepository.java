package com.loopers.domain.product;

import java.util.List;

public interface ProductRepository {
    ProductEntity save(ProductEntity productEntity);

    ProductWithLikeCount findDetailById(Long id);

    List<ProductWithLikeCount> findAllOrderBySortType(String sortType);
}
