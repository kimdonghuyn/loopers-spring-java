package com.loopers.interfaces.api.like;

import com.loopers.application.user.UserCriteria;
import com.loopers.application.user.UserFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.Gender;
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
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/like/products")
    @Nested
    class Like {
        private static final String ENDPOINT = "/api/v1/like/products";

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsPoints_whenGetPointsIsSuccessful() {
            // arrange
            UserCriteria.SignUp userCriteria = new UserCriteria.SignUp(
                    "loopers123",
                    "hyun",
                    "loopers123@naver.com",
                    "2002-10-10",
                    Gender.M
            );

            userFacade.signUp(userCriteria);

            String userId = "loopers123";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.GetResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.GetResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().loginId()).isEqualTo(userId),
                    () -> assertThat(response.getBody().data().amount()).isGreaterThanOrEqualTo(100)
            );
        }


        @DisplayName("`X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void returnsBadRequest_whenXUserIdHeaderIsMissing() {
            // arrange
            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.GetResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.GetResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }

    @DisplayName("POST /api/v1/points/charge")
    @Nested
    class Charge {
        private static final String ENDPOINT = "/api/v1/points/charge";

        @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnsChargedPoints_whenChargeIsSuccessful() {
            // arrange
            UserCriteria.SignUp userCriteria = new UserCriteria.SignUp(
                    "loopers123",
                    "hyun",
                    "loopers123@naver.com",
                    "2002-10-10",
                    Gender.M
            );

            userFacade.signUp(userCriteria);

            Long chargeAmount = 1000L;

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "loopers123");
            HttpEntity<Long> requestEntity = new HttpEntity<>(chargeAmount, headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.GetResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.GetResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().loginId()).isEqualTo("loopers123"),
                    () -> assertThat(response.getBody().data().amount()).isEqualTo(1100L) // 초기 100 + 충전 1000
            );

        }

        @DisplayName("존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returnsNotFound_whenUserDoesNotExist() {
            // arrange
            UserCriteria.SignUp userCriteria = new UserCriteria.SignUp(
                    "loopers123",
                    "hyun",
                    "loopers123@naver.com",
                    "2002-10-10",
                    Gender.M
            );

            userFacade.signUp(userCriteria);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "roopers123"); // 존재하지 않는 유저 ID
            HttpEntity<Long> requestEntity = new HttpEntity<>(100L, headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.GetResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.GetResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }
}
