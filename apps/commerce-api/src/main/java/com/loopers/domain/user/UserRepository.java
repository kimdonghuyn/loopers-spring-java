package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);

    boolean existsByUserId(String userId);

    boolean existsByLoginId(LoginId loginId);

    Optional<UserEntity> findByLoginId(LoginId loginId);
}
