package com.loopers.domain.like;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class LikeEntity {
    @Id
    private Long id;
    private Long userId;
    private Long productId;

    protected LikeEntity() {
    }

    public LikeEntity(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
