package com.loopers.domain.like;

import com.loopers.domain.user.LoginId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "likes")
@Getter
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private LoginId userId;
    private Long productId;

    protected LikeEntity() {
    }

    public LikeEntity(LoginId userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
