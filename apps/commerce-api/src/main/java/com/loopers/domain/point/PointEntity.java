package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor
public class PointEntity {
    @Id
    private String userId;
    private Long point;

    public PointEntity(String userId, Long point) {
        if (point != null && point <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "충전할 포인트는 0보다 커야 합니다.");
        }
        this.userId = userId;
        this.point = point;
    }
}
