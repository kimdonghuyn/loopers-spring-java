package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderFacade orderFacade;

    @Override
    @PostMapping("")
    public ApiResponse<OrderV1Dto.OrderResponse> order(
            @RequestHeader(value = "X-USER-ID", required = true) String loginId,
            @RequestBody OrderV1Dto.OrderRequest orderRequest
    ) throws CoreException {
        OrderCriteria.Order orderCriteria = orderRequest.toCriteria(loginId);
        OrderResult orderResult = orderFacade.order(orderCriteria);

        return ApiResponse.success(OrderV1Dto.OrderResponse.from(orderResult));
    }
}
