package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public void register(UserEntity userEntity) {
        if (userJpaRepository.existsByUserId(userEntity.getUserId())) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }

        userJpaRepository.save(userEntity);
    }
}
