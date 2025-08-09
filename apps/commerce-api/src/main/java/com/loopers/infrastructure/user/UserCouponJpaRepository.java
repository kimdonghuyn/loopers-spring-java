package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserCoupon;
import com.loopers.support.enums.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
    UserCoupon findByUserId(Long userId);

    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.status = :status, uc.usedAt = CURRENT_TIMESTAMP  WHERE uc.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") CouponStatus status);
}
