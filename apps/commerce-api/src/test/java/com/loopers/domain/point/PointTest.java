package com.loopers.domain.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointEntity;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserEntity;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {
    /**
     * 🧱 단위 테스트
     *
     * - [x] 0 이하의 정수로 포인트를 충전 시 실패한다.
     */

    @DisplayName("0 이하의 정수로 포인트를 충전 시 `400 Bad Request` 응답을 반환한다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void fail_whenChargePointIsZeroOrNegative(Long amount) {
        // arrange
        var userId = "loopers123";

        // act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            new Point(new LoginId("loopers123"), amount);
        });

        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
}
