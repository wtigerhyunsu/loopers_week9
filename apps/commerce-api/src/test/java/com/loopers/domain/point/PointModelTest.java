package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.point.embeded.Point;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PointModelTest {

  @DisplayName("포인트를 충전할때,")
  @Nested
  class Charge {

    @DisplayName("0 이하의 정수로 포인트를 충전하는 경우, `400 BadRequest`를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void throwsBadRequest_whenChargingZeroOrLessPoints(int intPoint) {
      // given
      BigInteger point = BigInteger.valueOf(intPoint);
      // when
      CoreException result = assertThrows(CoreException.class, () -> new Point().charge(point));
      // then
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("0 미만의 정수로 포인트를 사용하는 경우, `IllegalArgumentException`를 반환한다.")
    @Test
    void throwsBadRequest_whenUsingZeroLessPoints() {
      // given
      BigInteger point = BigInteger.valueOf(-1);
      // when&then
      Exception result = assertThrows(IllegalArgumentException.class, () -> new Point().use(point));
    }


    @DisplayName("포인트 저장시 포인트가 음수라면, `IllegalArgumentException`를 반환한다.")
    @Test
    void throwsBadRequest_whenSavingZeroLessPoints() {
      // given
      BigInteger point = BigInteger.valueOf(-1);
      // when&then
      Exception result = assertThrows(IllegalArgumentException.class, () -> new Point(point));

    }

    @DisplayName("포인트 저장시 계정 아이디가 존재한다면, 포인트는 0으로 리턴되어집니다.")
    @Test
    void returnPointIsZero_whenSavingExitsUserId() {
      // given
      String userId = "userId";
      // when
      PointModel pointModel = new PointModel(userId);
      // then
      assertThat(pointModel.getUserId()).isEqualTo(userId);
      assertThat(pointModel.getPoint()).isEqualTo(0);
    }


  }
}
