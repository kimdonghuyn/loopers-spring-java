package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
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
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {
    /**
     * *🌐 E2E 테스트**
     * <p>
     * 포인트 조회
     * - [x]  포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.
     * - [x]  `X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.
     *
     *
     * 포인트 충전
     * - [x]  존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.
     * - [x]  존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.
     */

    @Autowired
    private TestRestTemplate testRestTemplate;


    @DisplayName("GET /api/v1/points")
    @Nested
    class GetPoints {
        private static final String ENDPOINT = "/api/v1/points";

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsPoints_whenGetPointsIsSuccessful() {
            // arrange
            String userId = "loopers123";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(userId),
                    () -> assertThat(response.getBody().data().point()).isGreaterThanOrEqualTo(0)
            );
        }


        @DisplayName("`X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void returnsBadRequest_whenXUserIdHeaderIsMissing() {
            // arrange
            HttpEntity<Void> requestEntity = new HttpEntity<>(new HttpHeaders());

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }

    @DisplayName("POST /api/v1/points")
    @Nested
    class Charge {
        private static final String ENDPOINT = "/api/v1/points/charge";

        @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnsChargedPoints_whenChargeIsSuccessful() {
            // arrange
            Long point = 100L;
            Long chargeAmount = 1000L;
            String userId = "loopers123";

            PointV1Dto.PointRequest request = new PointV1Dto.PointRequest(
                    userId,
                    chargeAmount
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            HttpEntity<PointV1Dto.PointRequest> requestEntity = new HttpEntity<>(request, headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            point += chargeAmount;

            Long totalPoint = point;

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(userId),
                    () -> assertThat(response.getBody().data().point()).isGreaterThanOrEqualTo(totalPoint)
            );

        }

        @DisplayName("존재하지 않는 유저로 요청할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returnsNotFound_whenUserDoesNotExist() {
            // arrange
            String userId = "loopers_hyun";
            PointV1Dto.PointRequest request = new PointV1Dto.PointRequest(
                    userId,
                    1000L
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            HttpEntity<PointV1Dto.PointRequest> requestEntity = new HttpEntity<>(request, headers);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }
}
