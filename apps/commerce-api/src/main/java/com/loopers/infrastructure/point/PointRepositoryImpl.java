package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {
  private final PointJpaRepository pointRepository;


  @Override
  public Optional<PointModel> getWithLock(String userId) {
    return pointRepository.findByUserIdWithLock(userId);
  }

  @Override
  public Optional<PointModel> get(String userId) {
    return pointRepository.findByUserId(userId);
  }


  @Override
  public PointModel save(PointModel point) {
    return pointRepository.save(point);
  }
}
