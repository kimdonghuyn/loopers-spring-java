package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {
    @Override
    public PaymentInfo save(PaymentEntity payment) {
        return null;
    }
}
