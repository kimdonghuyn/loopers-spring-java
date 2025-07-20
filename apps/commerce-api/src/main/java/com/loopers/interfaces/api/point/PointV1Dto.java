package com.loopers.interfaces.api.point;

import com.loopers.domain.point.PointEntity;
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
        public static PointV1Dto.PointResponse from(PointEntity info) {
            return new PointV1Dto.PointResponse(
                    info.getUserId(),
                    info.getPoint()
            );
        }
    }
}
