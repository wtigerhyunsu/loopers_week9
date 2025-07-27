package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;

public class PointV1Dto {

  public record PointResponse(String userId, int point) {

    public static PointResponse from(PointInfo pointInfo) {
      return new PointResponse(
          pointInfo.userId(),
          pointInfo.point()
      );
    }
  }

  public record ChargeResponse(String userId, int point) {
      public static ChargeResponse from(PointInfo pointInfo) {
        return new ChargeResponse(pointInfo.userId(), pointInfo.point());
    }
  }

  public record ChargeRequest(int point) {}
}
