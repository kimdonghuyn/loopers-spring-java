package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.UserEntity;
import com.loopers.support.error.CoreException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {
    Optional<PointEntity> findByLoginId(LoginId loginId) throws CoreException;
}
