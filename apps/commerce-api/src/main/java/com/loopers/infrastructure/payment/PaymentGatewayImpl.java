package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentQuery;
import com.loopers.interfaces.api.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class PaymentGatewayImpl implements PaymentGateway {

    private final PgFeignClient pgFeignClient;

    private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";

    @Override
    @Retry(name = "pg-approve", fallbackMethod = "fallbackCardPayment")
    @CircuitBreaker(name = "pg-request-payment")
    public PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Card.Payment command) {

        PgSimulatorDto.CardPayment.Create.Request request = new PgSimulatorDto.CardPayment.Create.Request(
                command.orderKey(),
                command.cardType(),
                command.cardNo(),
                command.amount(),
                CALLBACK_URL
        );

        ApiResponse<PgSimulatorDto.CardPayment.Create.Response> response = pgFeignClient.requestPayment(command.loginId(), request);

        return response.data().toPaymentInfo();
    }

    @Override
    public PaymentInfo.Card.Get findCardPaymentResult(PaymentCommand.Card.Get command) {
        ApiResponse<PgSimulatorDto.CardPayment.Get.Response> response = pgFeignClient.getPayment(command.loginId(), new PgSimulatorDto.CardPayment.Get.Request(command.transactionKey()));
        return response.data().toPaymentInfo();
    }

    @Override
    public PaymentInfo.Card.Order findCardOrderResult(String orderKey) {
        ApiResponse<PgSimulatorDto.CardPayment.Order.Response> response
                = pgFeignClient.getOrderPayments(new PgSimulatorDto.CardPayment.Order.Request(orderKey));
        return response.data().toPaymentInfo();
    }

    public PaymentInfo.Card.Result fallbackCardPayment(PaymentCommand.Card.Payment command, Throwable t) {
        log.warn("PG 결제 요청 실패 - orderId={}, reason={}", command.orderKey(), t.toString());
        // 실패 시 도메인 표준 실패 결과를 반환
        return null;
    }
}
