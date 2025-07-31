package com.loopers.application.like;

import com.loopers.domain.like.LikeCommand;
import com.loopers.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final LikeService likeService;

    @Transactional
    public void like(LikeCriteria.Like likeCriteria) {
        likeService.like(likeCriteria.toCommand());
    }

    @Transactional
    public void unlike(LikeCriteria.Like likeCriteria) {
        likeService.unlike(likeCriteria.toCommand());
    }
}
