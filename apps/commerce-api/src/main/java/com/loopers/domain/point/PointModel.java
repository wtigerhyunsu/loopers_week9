package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.point.embeded.Point;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "point")
public class PointModel extends BaseEntity {

  private String userId;
  @Embedded
  private Point point;

  public PointModel() {
  }

  public PointModel(String userId) {
    this.userId = userId;
    this.point = new Point();
  }

  public PointModel(String userId, int point) {
    this.userId = userId;
    this.point = new Point(point);
  }

  public void charge(int point) {
    this.point = this.point.add(point);
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getPoint() {
    return point.getPoint();
  }


}
