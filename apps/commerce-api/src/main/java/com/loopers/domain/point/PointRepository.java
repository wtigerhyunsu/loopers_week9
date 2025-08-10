package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

  Optional<PointModel> getWithLock(String userId);
  Optional<PointModel> get(String userId);

  PointModel save(PointModel point);
}
