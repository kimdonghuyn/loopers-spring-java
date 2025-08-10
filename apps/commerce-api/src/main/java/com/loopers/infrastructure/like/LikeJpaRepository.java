package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long>
{
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from LikeEntity l where l.userId = :userId and l.productId = :productId")
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
