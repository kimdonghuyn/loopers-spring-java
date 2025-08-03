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
public class LikeV1ApiE2ETest {

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

    @DisplayName("POST /api/v1/like/products")
    @Nested
    class Like {
        private static final String ENDPOINT = "/api/v1/like/products/" + 1;

        @Test
        @DisplayName("사용자가 좋아요를 여러번 눌러도 중복되지 않고 하나의 좋아요로 처리된다.")
        void likeProduct() {
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

            ParameterizedTypeReference<ApiResponse<LikeV1Dto.LikeRequest>> responseType =
                    new ParameterizedTypeReference<>() {
                    };

            // act - 1차 요청
            ResponseEntity<ApiResponse<LikeV1Dto.LikeRequest>> firstResponse =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            // act - 2차 요청 (같은 요청 다시)
            ResponseEntity<ApiResponse<LikeV1Dto.LikeRequest>> secondResponse =
                    testRestTemplate.exchange(ENDPOINT, POST, requestEntity, responseType);

            // assert
            assertAll(
                    () -> assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(secondResponse.getBody()).isEqualTo(firstResponse.getBody())
            );
        }
    }
}
