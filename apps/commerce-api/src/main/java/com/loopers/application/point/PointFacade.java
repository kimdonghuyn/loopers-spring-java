package com.loopers.application.point;

import com.loopers.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public PointResult get(final String loginId) {
        return PointResult.from(pointService.get(loginId));
    }

    public PointResult charge(PointCriteria.Charge pointCriteria) {
        return PointResult.from(pointService.charge(pointCriteria.toCommand()));
    }
}
