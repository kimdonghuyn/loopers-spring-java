package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "Payment V1 API", description = "결제 관련 API")
public interface PaymentV1ApiSpec {
    @Operation(
            summary = "결제 콜백 처리",
            description = "PG사로부터 결제 결과를 전달받아 처리합니다."
    )
    ApiResponse<Object> callBack(
            @Schema(description = "헤더 사용자 로그인 ID", example = "user123")
            String loginId,

            @Schema(description = "결제 콜백 요청 정보", example = """
                    {
                      "orderId": "order123",
                      "transactionId": "txn12345",
                      "status": "SUCCESS",
                      "reason": "Payment completed successfully"
                    }
                    """)
            PaymentV1Dto.PG.CallbackRequest request
    );
}
