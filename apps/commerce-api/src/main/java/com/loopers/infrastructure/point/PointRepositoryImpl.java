package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public PointEntity save(final PointEntity pointEntity) {
        return pointJpaRepository.save(pointEntity);
    }

    @Override
    public Optional<PointEntity> findByLoginId(LoginId loginId) throws CoreException {
        return pointJpaRepository.findByLoginId(loginId);
    }

    @Override
    public Optional<PointEntity> findByLoginIdForUpdate(LoginId loginId) throws CoreException {
        return pointJpaRepository.findByLoginIdForUpdate(loginId);
    }
}
