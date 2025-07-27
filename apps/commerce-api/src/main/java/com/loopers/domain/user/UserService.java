package com.loopers.domain.user;

import com.loopers.application.example.user.UserResult;
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

    public UserInfo signUp(UserCommand.SignUp command) {
        UserEntity userEntity = new UserEntity(
                new LoginId(command.loginId()),
                command.name(),
                new Email(command.email()),
                new Birth(command.birth()),
                command.gender()
        );

        if (userJpaRepository.existsByLoginId(userEntity.getLoginId())) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }

        return UserInfo.from(userJpaRepository.save(userEntity));
    }

    public UserInfo getUserInfo(String loginId) {

        return userJpaRepository.findByLoginId(new LoginId(loginId))
                .map(UserInfo::from)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }
}
