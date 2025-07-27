package com.loopers.domain.point;

public class PointCommand {

    public record Init(
            String loginId,
            Long amount
    ) {}

    public record Charge(
            String loginId,
            Long amount
    ) {}
}
