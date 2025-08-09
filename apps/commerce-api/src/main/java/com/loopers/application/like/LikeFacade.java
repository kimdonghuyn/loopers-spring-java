package com.loopers.application.like;

import com.loopers.domain.like.LikeCommand;
import com.loopers.domain.like.LikeEntity;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final UserService userService;
    private final LikeService likeService;

    @Transactional
    public void like(LikeCriteria.Like likeCriteria) {
        UserInfo userInfo = userService.getUserInfo(likeCriteria.loginId());

        likeService.like(likeCriteria.toCommand(userInfo.userId()));
    }

    @Transactional
    public void unlike(LikeCriteria.Like likeCriteria) {
        UserInfo userInfo = userService.getUserInfo(likeCriteria.loginId());

        likeService.unlike(likeCriteria.toCommand(userInfo.userId()));
    }
}
