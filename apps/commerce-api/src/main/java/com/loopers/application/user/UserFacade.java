package com.loopers.application.user;

import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;
    private final PointService pointService;

    public UserResult signUp(final UserCriteria.SignUp criteria) {
        final UserResult userResult = UserResult.from(userService.signUp(criteria.toCommand()));

        pointService.initPoint(new PointCommand.Init(criteria.loginId(), 100L));

        return userResult;
    }

    public UserResult getUserInfo(final String userId) {
        return UserResult.from(userService.getUserInfo(userId));
    }
}
