package com.loopers.domain.order;

import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    @DisplayName("상품 수량이 0보다 작거나 같으면, 주문 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {
            1,
            -1,
            0
    })
    void fail_whenQuantityIsZeroOrLess(int quantity) {
        // act
        final CoreException exception = assertThrows(CoreException.class, () -> {
            new Quantity(quantity);
        });

        //assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

}
