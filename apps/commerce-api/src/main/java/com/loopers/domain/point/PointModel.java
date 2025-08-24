package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.point.embeded.Point;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PointModel extends BaseEntity {

  private String userId;
  @Embedded
  private Point point;

  public PointModel(String userId) {
    validate(userId);
    this.userId = userId;
    this.point = new Point();
  }

  public PointModel(String userId, BigInteger point) {
    validate(userId);
    this.userId = userId;
    this.point = new Point(point);
  }

  private void validate(String userId) {
    if (userId == null) {
      throw new NoSuchElementException("포인트 저장시 계정아이디는 필수입니다.");
    }
  }

  public void charge(BigInteger point) {
    this.point = this.point.charge(point);
  }
  
  public void use(BigInteger point) {
    this.point = this.point.use(point);
  }


  public BigInteger getPoint() {
    return point.getPoint();
  }


}
