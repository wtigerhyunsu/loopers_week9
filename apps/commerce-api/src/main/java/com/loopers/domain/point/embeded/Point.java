package com.loopers.domain.point.embeded;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Point {
  private int point;

  public Point() {
    this.point = 0;
  }

  public Point add(int point) {
    if (point <= 0) {
      throw new CoreException(ErrorType.BAD_REQUEST, "0이하로 포인트를 충전할 수 없습니다.");
    }
    return new Point(this.point + point);
  }

  public Point(int point) {
    this.point = point;
  }

}
