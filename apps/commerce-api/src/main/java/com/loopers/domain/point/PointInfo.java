package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;

public record PointInfo(
        Long id,
        LoginId loginId,
        Long amount
) {
    public static PointInfo from(final PointEntity pointEntity) {
        return new PointInfo(
                pointEntity.getId(),
                pointEntity.getLoginId(),
                pointEntity.getAmount()
        );
    }
}
