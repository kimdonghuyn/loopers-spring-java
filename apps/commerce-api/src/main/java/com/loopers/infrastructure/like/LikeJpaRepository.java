package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long>
{
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
