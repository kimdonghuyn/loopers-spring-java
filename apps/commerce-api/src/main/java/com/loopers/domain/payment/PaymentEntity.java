package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.support.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class PaymentEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String orderKey;

    @Column(unique = true)
    private String transactionKey;

    private String cardType;

    private String cardNo;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String reason;

    public PaymentEntity(String transactionKey, String orderId, String cardType, String cardNo, Long amount, String status, String reason) {
        this.transactionKey = transactionKey;
        this.orderKey = orderId;
        this.cardType = cardType;
        this.cardNo = cardNo;
        this.amount = amount;
        this.status = PaymentStatus.valueOf(status);
        this.reason = reason;
    }

    public static PaymentEntity create(PaymentCommand.CreatePayment command) {
        return new PaymentEntity(
                command.transactionKey(),
                command.orderId(),
                command.cardType(),
                command.cardNo(),
                command.amount(),
                command.status(),
                command.reason()
        );
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}
