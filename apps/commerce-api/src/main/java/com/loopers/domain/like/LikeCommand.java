package com.loopers.domain.like;


import com.loopers.domain.user.LoginId;
import com.loopers.support.Gender;

public class LikeCommand {
    public record Like(
            String loginId,
            Long productId
    ) {}
}
