package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.LoginId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor
public class PointEntity extends BaseEntity {
    private LoginId loginId;
    private Long amount;

    public PointEntity(LoginId loginId, Long amount) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID는 null일 수 없습니다.");
        }

        if (amount != null && amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0보다 커야 합니다.");
        }
        this.loginId = loginId;
        this.amount = amount;
    }

    public void charge(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0보다 커야 합니다.");
        }
        this.amount += amount;
    }

    public void use(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 0보다 커야 합니다.");
        }
        if (this.amount < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.amount -= amount;
    }
}
