package com.loopers.application.point;

import com.loopers.domain.point.PointCommand;

import java.math.BigDecimal;

public class PointCriteria {

    public record Charge(
            String loginId,
            BigDecimal amount
    ) {
        public PointCommand.Charge toCommand() {
            return new PointCommand.Charge(loginId, amount);
        }
    }
}
