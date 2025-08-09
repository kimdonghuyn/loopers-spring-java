package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {
    Optional<PointEntity> findByLoginId(LoginId loginId) throws CoreException;

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PointEntity p WHERE p.loginId = :loginId")
    Optional<PointEntity> findByLoginIdForUpdate(LoginId loginId) throws CoreException;
}
