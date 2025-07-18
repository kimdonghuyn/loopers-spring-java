package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    /**
     * 🧱 단위 테스트
     * <p>
     * - [x]  ID 가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.
     * - [x]  이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.
     * - [x]  생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.
     */

    @DisplayName("ID 가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "loopers123",
            "1",
            "",
            "동현",
            "동현동현동현동현동현동현동현",
            "hyun_____k",
    })
    void fail_whenIdFormatIsInvalid(String userId) {

        //arrange
        final var name = "hyun";
        final var email = "loopers123@naver.com";
        final var gender = "F";
        final var birth = "2020-02-02";

        //act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            new UserEntity(userId, name, gender, email, birth);
        });

        //assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "loopers123@naver.com",
            "",
            "loppers123@naver",
    })
    void fail_whenUserIdFormatIsInvalid(String email) {
        //arrange
        final var userId = "loopers123";
        final var name = "hyun";
        final var gender = "F";
        final var birth = "2020-02-02";

        //act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            new UserEntity(userId, name, gender, email, birth);
        });

        //assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "2020-01-02",
            "",
            "20-01-02",
            "2020-1-02",
            "2020-01-2",
            "--",
            "20200102",
    })
    void fail_whenDateFormatIsInvalid(String birth) {
        //arrange
        final var userId = "loopers123";
        final var name = "hyun";
        final var gender = "F";
        final var email = "loopers123@naver.com";

        //act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            new UserEntity(userId, name, gender, email, birth);
        });

        //assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
}
