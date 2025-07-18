package com.loopers.domain.example.point;

import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointServiceIntegrationTest {
    /**
     * 🔗 통합 테스트
     * <p>
     * 포인트 조회
     * - [x]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
     * - [ ]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     *
     * 포인트 충전
     * - [x] 존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.
     */

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
    @Test
    void returnsPoints_whenGetPointsIsSuccessful() {
        // arrange
        UserEntity userEntity = new UserEntity(
                "loopers123",
                "hyun",
                "F",
                "loopers@naver.com",
                "2002-10-10"
        );

        userService.register(userEntity);

        // act
        ApiResponse<PointV1Dto.PointResponse> response = pointService.getUserPoint(userService.getUserInfo(userEntity.getUserId()).userId());

        //assert
        assertAll(
                () -> assertThat(response.data().userId()).isEqualTo(userEntity.getUserId()),
                () -> assertThat(response.data().point()).isEqualTo(100)
        );
    }

    @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
    @Test
    void returnsNotFound_whenUserDoesNotExist() {
        // arrange
        String nonExistentUserId = "loopers_hyun";

        // act
        CoreException exception = assertThrows(CoreException.class, () -> {
            pointService.getUserPoint(nonExistentUserId);
        });

        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("존재하지 않는 유저 ID 로 포인트 충전을 시도할 경우, 실패한다.")
    @Test
    void fail_whenChargePointForNonExistentUser() {
        // arrange
        String nonExistentUserId = "loopers_hyun";
        long chargeAmount = 100L;

        // act
        CoreException exception = assertThrows(CoreException.class, () -> {
            pointService.chargePoint(nonExistentUserId, chargeAmount);
        });

        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }
}
