package com.loopers.application.point;

import com.loopers.domain.point.PointInfo;

import java.math.BigDecimal;

public record PointResult(
        Long id,
        String loginId,
        BigDecimal amount
) {
    public static PointResult from(final PointInfo pointInfo) {
        return new PointResult(
                pointInfo.id(),
                pointInfo.loginId().getLoginId(),
                pointInfo.amount()
        );
    }
}
