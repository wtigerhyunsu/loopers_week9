package com.loopers.domain.point;

public interface PointRepository {

  PointModel get(String userId);

  PointModel charge(String userId, int point);
}
