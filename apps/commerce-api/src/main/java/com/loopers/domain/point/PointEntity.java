package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.LoginId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PointEntity extends BaseEntity {
    private LoginId loginId;
    private BigDecimal amount;

    public PointEntity(final LoginId loginId, final BigDecimal amount) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 ID는 null일 수 없습니다.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("포인트 금액은 null이거나 0보다 작을 수 없습니다.");
        }
        this.loginId = loginId;
        this.amount = amount;
    }

    public void charge(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전할 포인트는 0보다 커야 합니다.");
        }

        this.amount = this.amount.add(amount);
    }

    public void use(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("사용할 포인트는 0보다 커야 합니다.");
        }
        if (this.amount.compareTo(amount) < 0) {
            throw new IllegalArgumentException("사용할 포인트가 현재 보유 포인트보다 많습니다.");
        }

        this.amount = this.amount.subtract(amount);
    }
}
