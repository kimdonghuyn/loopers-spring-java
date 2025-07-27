package com.loopers.application.point;

import com.loopers.domain.point.PointInfo;

public record PointResult(
        Long id,
        String loginId,
        Long amount
) {
    public static PointResult from(final PointInfo pointInfo) {
        return new PointResult(
                pointInfo.id(),
                pointInfo.loginId().getLoginId(),
                pointInfo.amount()
        );
    }
}
