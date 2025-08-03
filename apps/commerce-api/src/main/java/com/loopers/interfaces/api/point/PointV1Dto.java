package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointChargeInfo;
import com.loopers.application.point.PointInfo;
import java.math.BigInteger;

public class PointV1Dto {

  public record PointResponse(String userId, BigInteger point) {

    public static PointResponse from(PointInfo pointInfo) {
      return new PointResponse(
          pointInfo.userId(),
          pointInfo.point()
      );
    }
  }

  public record ChargeResponse(String userId, BigInteger point) {
      public static ChargeResponse from(PointChargeInfo chargeInfo) {
        return new ChargeResponse(chargeInfo.userId(), chargeInfo.point());
    }
  }

  public record ChargeRequest(BigInteger point) {}
}
