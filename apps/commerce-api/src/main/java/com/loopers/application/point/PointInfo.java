package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import java.math.BigInteger;

public record PointInfo(String userId, BigInteger point) {

  public static PointInfo from(PointModel point) {
    return new PointInfo(
        point.getUserId(),
        point.getPoint()
    );
  }
}
