package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;

import java.util.Optional;

public interface PointRepository {
    Optional<PointEntity> findByLoginId(LoginId loginId) throws CoreException;

    Optional<PointEntity> findByLoginIdForUpdate(LoginId loginId) throws CoreException;

    PointEntity save(PointEntity pointEntity) throws CoreException;
}
