package com.loopers.interfaces.api.user;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserInfo;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.enums.Gender;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {
    /**
     * *🌐 E2E 테스트**
     * - [x]  회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.
     * - [x]  회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.
     * - [x]  내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.
     * - [x]  존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.
     */

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Join {
        private static final String ENDPOINT = "/api/v1/users";

        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserInfo_whenRegistrationIsSuccessful() {
            // arrange
            UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
                    "loopers123",
                    "hyun",
                    "loopers@naver.com",
                    "2002-10-10",
                    Gender.F
            );

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(signUpRequest), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().name()).isEqualTo(signUpRequest.name()),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(Gender.F)
            );
        }

        @DisplayName("회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void returnsBadRequest_whenGenderIsMissing() {
            // arrange
            UserV1Dto.SignUpRequest signUpRequest = new UserV1Dto.SignUpRequest(
                    "loopers",
                    "hyun",
                    "2002-10-10",
                    "loopers@naver.com",
                    null
                    );

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(signUpRequest), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
                    () -> assertThat(response.getBody().data()).isNull()
            );
        }

    }

    @DisplayName("GET /api/v1/users/me")
    @Nested
    class MyInfo {
        private static final String ENDPOINT = "/api/v1/users/me";

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserInfo_whenMyInfoIsRetrievedSuccessfully() {
            // arrange
            String existUserId = "loopers123";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", existUserId);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            UserCommand.SignUp user = new UserCommand.SignUp(
                    existUserId,
                    "hyun",
                    "loopers@nvaer.com",
                    "2002-10-10",
                    Gender.F
            );

            final UserInfo userInfo = userService.signUp(user);


            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().loginId()).isEqualTo(existUserId)
            );

        }


        @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returnsNotFound_whenUserDoesNotExist() {
            // arrange
            String nonExistentUserId = "ropers1234";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", nonExistentUserId);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            UserCommand.SignUp user = new UserCommand.SignUp(
                    "loopers123",
                    "hyun",
                    "loopers@nvaer.com",
                    "2002-10-10",
                    Gender.F
            );

            final UserInfo userInfo = userService.signUp(user);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody().data()).isNull()
            );

        }

    }
}
