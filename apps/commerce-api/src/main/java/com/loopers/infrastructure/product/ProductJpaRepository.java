package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductWithLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                JOIN FETCH p.brand b
                LEFT JOIN LikeEntity l ON l.productId = p.id
                GROUP BY p, b
                ORDER BY
                    CASE WHEN :sortType = 'LATEST' THEN p.id END DESC,
                    CASE WHEN :sortType = 'PRICE_ASC' THEN p.price END ASC,
                    CASE WHEN :sortType = 'LIKES_DESC' THEN COUNT(l.id) END DESC
            """)
    List<ProductWithLikeCount> findAllOrderBySortType(@Param("sortType") String sortType);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                JOIN FETCH p.brand b
                LEFT JOIN LikeEntity l ON l.productId = p.id
                WHERE p.id = :productId
                GROUP BY p, b
            """)
    ProductWithLikeCount findProductById(@Param("productId") Long productId);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                JOIN FETCH p.brand b
                LEFT JOIN LikeEntity l ON l.productId = p.id
                WHERE p.id IN :productIds
                GROUP BY p, b
            """)
    List<ProductWithLikeCount> findAllById(List<Long> productIds);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                JOIN FETCH p.brand b
                JOIN LikeEntity l ON l.productId = p.id AND l.userId = :userId
                GROUP BY p, b
            """)
    List<ProductWithLikeCount> findLikedProductsByUserId(@Param("userId") Long userId);
}
