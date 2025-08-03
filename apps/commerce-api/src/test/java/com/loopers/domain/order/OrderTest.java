package com.loopers.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {
    @DisplayName("상품 수량이 0보다 작거나 같으면, 주문 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0
    })
    void fail_whenQuantityIsZeroOrLess(int quantity) {
        // act
        assertThrows(IllegalArgumentException.class, () ->{
            new Quantity(quantity);
        });
    }

}
