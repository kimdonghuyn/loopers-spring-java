package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointCriteria;
import com.loopers.application.point.PointResult;
import com.loopers.domain.point.PointEntity;
import jakarta.validation.constraints.NotNull;

public class PointV1Dto {

    public record ChargeRequest(
            @NotNull
            String loginId,
            Long amount
    ) {
        public PointCriteria.Charge  toCriteria(PointV1Dto.ChargeRequest request) {
            return new PointCriteria.Charge(request.loginId(), request.amount());
        }
    }

    public record GetResponse(
            Long id,
            String loginId,
            Long amount
    ) {
        public static PointV1Dto.GetResponse from(PointResult info) {
            return new PointV1Dto.GetResponse(
                    info.id(),
                    info.loginId(),
                    info.amount()
            );
        }
    }
}
