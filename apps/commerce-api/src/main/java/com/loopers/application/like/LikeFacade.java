package com.loopers.application.like;

import com.loopers.domain.like.LikeCommand;
import com.loopers.domain.like.LikeEntity;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final UserService userService;
    private final LikeService likeService;

    @Transactional
    public void like(LikeCriteria.Like likeCriteria) {
        UserInfo userInfo = userService.getUserInfo(likeCriteria.loginId());
        LikeCommand.Like command = likeCriteria.toCommand(userInfo.userId());

        likeService.like(command);
    }

    @Transactional
    public void unlike(LikeCriteria.Like likeCriteria) {
        UserInfo userInfo = userService.getUserInfo(likeCriteria.loginId());
        LikeCommand.Like command = likeCriteria.toCommand(userInfo.userId());

        likeService.unlike(command);
    }
}
