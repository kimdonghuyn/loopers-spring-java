package com.loopers.infrastructure.payment;

import com.loopers.interfaces.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "pgSimulatorCommandClient",
        url = "${pg.base-url}",
        configuration = PgFeignClientConfig.class
)
public interface PgFeignClient {
    @PostMapping("/api/v1/payments")
    ApiResponse<PgSimulatorDto.CardPayment.Create.Response> requestPayment(
            @RequestHeader("X-USER-ID") String loginId,
            @RequestBody PgSimulatorDto.CardPayment.Create.Request request
    );

    @GetMapping("/api/v1/payments/{transactionKey}")
    ApiResponse<PgSimulatorDto.CardPayment.Get.Response> getPayment(
            @RequestHeader("X-USER-ID") String loginId,
            @RequestBody PgSimulatorDto.CardPayment.Get.Request command
    );

    @GetMapping("/api/v1/payments/orders/{orderKey}")
    ApiResponse<PgSimulatorDto.CardPayment.Order.Response> getOrderPayments(
            @RequestBody PgSimulatorDto.CardPayment.Order.Request command
    );
}
