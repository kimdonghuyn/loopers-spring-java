package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {
    /**
     * 🔗 통합 테스트
     *
     * 회원가입
     * - [x]  회원 가입시 User 저장이 수행된다. ( spy 검증 )
     * - [x]  이미 가입된 ID 로 회원가입 시도 시, 실패한다.
     *
     *  회원 정보 조회
     * - [x]  해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.
     * - [x]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     *
     */


    @SpyBean
    private UserJpaRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입시 User 저장이 수행된다.")
    @Test
    void userIsSavedWhenRegistering() {
        // arrange
        var userId = "loopers123";
        var name = "hyun";
        Gender gender =Gender.F;
        var email = "loopers@naver.com";
        var birth = "2002-10-10";

        UserEntity user = new UserEntity(userId, name, gender, email, birth);

        // act
        userService.register(user);

        // assert
        verify(userRepository).save(any(UserEntity.class));
    }

    @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
    @Test
    void registrationFailsWhenIdAlreadyExists() {
        // arrange
        UserEntity user = new UserEntity(
                "loopers123",
                "hyun",
                Gender.F,
                "loopers@naver.com",
                "2002-10-10"
        );

        userService.register(user);

        // act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            userService.register(user);
        });

        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);}


    @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
    @Test
    void returnsUserInfoWhenUserExists() {
        // arrange
        String userId = "loopers123";
        UserEntity user = new UserEntity(
                "loopers123",
                "hyun",
                Gender.F,
                "loopers@naver.com",
                "2002-10-10"
        );

        userService.register(user);

        // act
        UserEntity response = userService.getUserInfo(userId);

        // assert
        assertAll(
                () -> assertThat(response.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(response.getName()).isEqualTo(user.getName()),
                () -> assertThat(response.getGender()).isEqualTo(user.getGender()),
                () -> assertThat(response.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(response.getBirth()).isEqualTo(user.getBirth())
        );
    }

    @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenUserDoesNotExist() {
        // arrange
        String userId = "loopers_hyun";

        // act & assert
        CoreException exception = assertThrows(CoreException.class, () -> {
            userService.getUserInfo(userId);
        });

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }
}
