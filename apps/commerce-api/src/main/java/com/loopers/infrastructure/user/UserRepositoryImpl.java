package com.loopers.infrastructure.user;

import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userJpaRepository.save(userEntity);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return false;
    }

    @Override
    public boolean existsByLoginId(LoginId loginId) {
        return userJpaRepository.existsByLoginId(loginId);
    }

    @Override
    public Optional<UserEntity> findByLoginId(LoginId loginId) {
        return userJpaRepository.findByLoginId(loginId);
    }
}
