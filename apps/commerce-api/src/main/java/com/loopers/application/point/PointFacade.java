package com.loopers.application.point;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;

    @Transactional(readOnly = true)
    public PointResult get(final String loginId) {
        return PointResult.from(pointService.get(loginId));
    }

    @Transactional
    public PointResult charge(PointCriteria.Charge pointCriteria) {
        return PointResult.from(pointService.charge(pointCriteria.toCommand()));
    }
}
