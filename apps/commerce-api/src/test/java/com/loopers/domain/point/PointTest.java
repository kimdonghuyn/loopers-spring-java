package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {
    /**
     * 🧱 단위 테스트
     *
     * - [x] 0 이하의 정수로 포인트를 충전 시 실패한다.
     */

    @DisplayName("0 이하의 정수로 포인트를 충전 시 `IllegalArgument Exception` 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void fail_whenChargePointIsZeroOrNegative(Long amount) {
        // arrange
        var userId = "loopers123";

        // act
        assertThrows(IllegalArgumentException.class, () -> {
            new Point(new LoginId("loopers123"), amount);
        });
    }
}
