package com.loopers.domain.point.embeded;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import java.math.BigInteger;
import lombok.Getter;

@Embeddable
@Getter
public class Point {
  private BigInteger point;

  public Point() {
    this.point = BigInteger.ZERO;
  }

  // 충전
  public Point charge(BigInteger point) {
    if (point.compareTo(BigInteger.ZERO) <= 0) {
      throw new CoreException(ErrorType.BAD_REQUEST, "0이하로 포인트를 충전할 수 없습니다.");
    }
    return new Point(this.point.add(point));
  }

  // 사용
  public Point use(BigInteger point) {
    if (point.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("0미만으로 포인트를 사용할 수 없습니다.");
    }
    return new Point(this.point.subtract(point));
  }


  public Point(BigInteger point) {
    if (point.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("포인트는 0미만일 수 없습니다.");
    }

    this.point = point;
  }

}
