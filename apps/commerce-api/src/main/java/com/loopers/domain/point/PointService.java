package com.loopers.domain.point;

import com.loopers.domain.user.LoginId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;

    public PointInfo get(String loginId) throws CoreException {

        return pointRepository.findByLoginId(new LoginId(loginId))
                .map(PointInfo::from)
                .orElse(null);
    }

    public void initPoint(PointCommand.Init pointCommand) throws CoreException {
        PointEntity pointEntity = new PointEntity(new LoginId(pointCommand.loginId()), pointCommand.amount());
        PointInfo.from(pointRepository.save(pointEntity));
    }

    public PointInfo charge(PointCommand.Charge pointCommand) throws CoreException {
        Optional<PointEntity> pointEntityOptional = pointRepository.findByLoginId(new LoginId(pointCommand.loginId()));

        if (pointEntityOptional.isPresent()) {
            PointEntity pointEntity = pointEntityOptional.get();
            pointEntity.charge(pointCommand.amount());
            return PointInfo.from(pointRepository.save(pointEntity));
        } else {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
    }

    @Transactional
    public void use(PointCommand.Use pointCommand) {
        PointEntity point = pointRepository.findByLoginIdForUpdate(new LoginId(pointCommand.loginId()))
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "포인트 계정이 존재하지 않습니다."));
        point.use(pointCommand.amount());
    }
}
