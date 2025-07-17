package com.loopers.domain.user;

public interface UserRepository {
    void register(UserEntity userEntity);

    boolean existsByUserId(String userId);
}
