package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.support.Gender;
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

    public UserV1Dto.UserResponse getUserInfo(String userId) {
        UserV1Dto.UserResponse response = new UserV1Dto.UserResponse(
                "loopers123",
                "hyun",
                 Gender.F,
                "loopers@naver.com",
                "2002-10-10"
        );

        if (response.userId().equals(userId)) {
            return response;
        } else {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }
    }
}
