package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductWithBrand;
import com.loopers.domain.product.ProductWithLikeCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                LEFT JOIN LikeEntity l ON l.productId = p.id
                GROUP BY p
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
                LEFT JOIN LikeEntity l ON l.productId = p.id
                WHERE p.id = :productId
                GROUP BY p
            """)
    ProductWithLikeCount findProductById(@Param("productId") Long productId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT p
                FROM ProductEntity p
                WHERE p.id = :productId
            """)
    Optional<ProductEntity> findByIdForUpdate(@Param("productId") Long productId);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                LEFT JOIN LikeEntity l ON l.productId = p.id
                WHERE p.id IN :productIds
                GROUP BY p
            """)
    List<ProductWithLikeCount> findAllById(List<Long> productIds);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithLikeCount(
                    p,
                    COUNT(l.id)
                )
                FROM ProductEntity p
                JOIN LikeEntity l ON l.productId = p.id AND l.userId = :userId
                GROUP BY p
            """)
    List<ProductWithLikeCount> findLikedProductsByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT new com.loopers.domain.product.ProductWithBrand(
                    p,
                    b
                )
                FROM ProductEntity p
                JOIN BrandEntity b ON p.brandId = b.id
                WHERE b.id = :brandId
            """)
    List<ProductWithBrand> findAllByBrandId(@Param("brandId") Long brandId, Pageable pageable);
}
