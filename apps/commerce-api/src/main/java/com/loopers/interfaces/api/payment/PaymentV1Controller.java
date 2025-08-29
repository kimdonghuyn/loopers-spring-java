package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCriteria;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Log4j2
public class PaymentV1Controller implements PaymentV1ApiSpec {

    private final PaymentFacade paymentFacade;

    public PaymentV1Controller(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @Override
    @PostMapping("/callback")
    public ApiResponse<Object> callBack(
            @RequestHeader("X-USER-ID") String loginId,
            @RequestBody PaymentV1Dto.PG.CallbackRequest request
    ) {
        try {
            validateCallback(request);

            PaymentCriteria.createPayment criteria = PaymentCriteria.createPayment.toCriteria(
                    loginId,
                    request.transactionKey(),
                    request.status(),
                    request.reason()
            );

            paymentFacade.createPayment(criteria);

            log.info("결제 콜백 처리 완료 - transactionKey: {}", request.transactionKey());
            return ApiResponse.success();
        } catch (CoreException e) {
            return ApiResponse.fail(ErrorType.BAD_REQUEST.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("콜백 처리 실패 - transactionKey: {}", request.transactionKey(), e);
            return ApiResponse.fail(ErrorType.INTERNAL_ERROR.getCode(), "콜백 처리 실패");
        }
    }

    private void validateCallback(PaymentV1Dto.PG.CallbackRequest request) {
        if (request.status().equals("FAIL")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 실패 사유: " + request.reason());
        }
    }
}
