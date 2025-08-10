package com.loopers.domain.point;

import java.math.BigDecimal;

public class PointCommand {

    public record Init(
            String loginId,
            BigDecimal amount
    ) {}

    public record Charge(
            String loginId,
            BigDecimal amount
    ) {}

    public record Use(
            String loginId,
            BigDecimal amount
    ){}
}
