package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Index;
import lombok.Getter;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_likes_user_product", columnNames = {"user_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_likes_user_product", columnList = "user_id, product_id")
        }
)
@Getter
public class LikeEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    protected LikeEntity() {
    }

    public LikeEntity(Long userId, Long productId) {
        if (userId == null || productId == null) {
            throw new IllegalArgumentException("userId 와 productId는 null일 수 없습니다.");
        }
        this.userId = userId;
        this.productId = productId;
    }
}
