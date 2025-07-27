package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;

@Getter
public class Point {
    private LoginId loginId;
    private Long amount;

    protected Point() {
    }

    public Point(
            final LoginId loginId,
            final Long amount
    ) {
        if (amount != null && amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "충전할 포인트는 0보다 커야 합니다.");
        }
        this.loginId = loginId;
        this.amount = amount;
    }
}
