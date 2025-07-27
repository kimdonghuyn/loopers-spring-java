package com.loopers.application.point;

import com.loopers.domain.point.PointCommand;

public class PointCriteria {

    public record Charge(
            String loginId,
            Long amount
    ) {
        public PointCommand.Charge toCommand() {
            return new PointCommand.Charge(loginId, amount);
        }
    }
}
