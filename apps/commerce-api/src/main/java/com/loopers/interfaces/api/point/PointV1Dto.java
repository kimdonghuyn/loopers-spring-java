package com.loopers.interfaces.api.point;

import jakarta.validation.constraints.NotNull;

public class PointV1Dto {

    public record PointRequest(
            @NotNull
            String userId,
            Long chargePointAmount
    ) {
    }

    public record PointResponse(
            String userId,
            Long point
    ) {
    }
}
