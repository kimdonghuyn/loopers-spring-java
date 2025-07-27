package com.loopers.application.example.user;

import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;
//    private final PointService pointService;

    public UserResult signUp(final UserCriteria.SignUp criteria) {
        final UserResult userResult = UserResult.from(userService.signUp(criteria.toCommand()));

//        pointService.create(new PointCommand.Create(userResult.id(), 0L));

        return userResult;
    }

    public UserResult getUserInfo(final String userId) {
        return UserResult.from(userService.getUserInfo(userId));
    }
}
