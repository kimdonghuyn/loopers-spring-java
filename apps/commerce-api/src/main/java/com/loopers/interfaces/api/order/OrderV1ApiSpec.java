package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order V1 API", description = "주문 관련 API")
public interface OrderV1ApiSpec {

    @Operation(
            summary = "주문 및 결제",
            description = "주문 및 결제 요청"
    )
    ApiResponse<OrderV1Dto.OrderResponse> order(String loginId, OrderV1Dto.OrderRequest orderRequest) throws CoreException;
}
