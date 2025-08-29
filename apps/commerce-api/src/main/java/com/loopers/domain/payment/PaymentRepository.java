package com.loopers.domain.payment;

public interface PaymentRepository {
    PaymentInfo save(PaymentEntity payment);
}
