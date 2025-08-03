package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "productId"})
        }
)
@Getter
public class LikeEntity extends BaseEntity {

    private Long userId;
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
