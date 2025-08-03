package com.loopers.domain.like;

import com.loopers.application.like.LikeResult;
import com.loopers.domain.user.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeInfo like(LikeCommand.Like likeCommand) {
        return LikeInfo.from(likeRepository.save(new LikeEntity(new LoginId(likeCommand.loginId()), likeCommand.productId())));
    }

    public void unlike(LikeCommand.Like likeCommand) {
        likeRepository.deleteByProductId(new LikeEntity(new LoginId(likeCommand.loginId()), likeCommand.productId()));
    }
}
