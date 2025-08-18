package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;

import java.math.BigDecimal;

public record PointInfo(
        Long id,
        LoginId loginId,
        BigDecimal amount
) {
    public static PointInfo from(final PointEntity pointEntity) {
        return new PointInfo(
                pointEntity.getId(),
                pointEntity.getLoginId(),
                pointEntity.getAmount()
        );
    }
}
