package com.loopers.domain.user;

import com.loopers.support.Gender;

public record UserInfo(
        Long id,
        LoginId loginId,
        String name,
        Email email,
        Birth birth,
        Gender gender
) {
    public static UserInfo from(final UserEntity userEntity) {
        return new UserInfo(
                userEntity.getId(),
                userEntity.getLoginId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getBirth(),
                userEntity.getGender()
        );
    }
}
