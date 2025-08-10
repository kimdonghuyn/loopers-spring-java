package com.loopers.domain.payment;

import com.loopers.support.enums.OrderStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {

    public OrderStatus pay(PaymentCommand.Payment paymentCriteria) {

        PaymentCommand.Payment paymentCommand = new PaymentCommand.Payment(paymentCriteria.userId(), paymentCriteria.totalPrice());
        // 외부 결제 시스템과 연동하여 결제 처리
         boolean paymentSuccess = true; // 성공 했다고 가정

         if (!paymentSuccess) {
             throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 처리에 실패했습니다. 다시 시도해주세요.");
         }

         return OrderStatus.COMPLETED;
    }
}
