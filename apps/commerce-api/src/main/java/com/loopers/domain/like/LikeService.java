package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final EntityManager em;

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public void like(LikeCommand.Like likeCommand) {
        if (isExistLike(likeCommand)) return;

        try {
            likeRepository.save(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
            em.flush();
        } catch (DataIntegrityViolationException | ConstraintViolationException ignored) {
        }
    }

    @Transactional
    public void unlike(LikeCommand.Like likeCommand) {
        likeRepository.deleteByUserIdAndProductId(likeCommand.userId(), likeCommand.productId());
    }

    private boolean isExistLike(LikeCommand.Like likeCommand) {
        return likeRepository.existsByUserIdAndProductId(new LikeEntity(likeCommand.userId(), likeCommand.productId()));
    }
}
