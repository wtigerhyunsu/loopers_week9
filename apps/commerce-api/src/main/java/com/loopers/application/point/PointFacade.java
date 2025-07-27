package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

  private final PointService pointService;

  public PointInfo get(String userId) {
    PointModel point = pointService.get(userId);
    return PointInfo.from(point);
  }

  public PointInfo charge(String userId, int point) {
    PointModel model = pointService.charge(userId, point);
    return PointInfo.from(model);
  }

}
