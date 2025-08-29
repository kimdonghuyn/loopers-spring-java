package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("POINT")
public class PointPayment implements PaymentStrategy {

    private final PointService pointService;

    public PointPayment(PointService pointService) {
        this.pointService = pointService;
    }

    @Override
    public void processPayment(PaymentCommand.Payment payment) {
        pointService.use(new PointCommand.Use(payment.loginId(), BigDecimal.valueOf(payment.amount())));
    }
}
