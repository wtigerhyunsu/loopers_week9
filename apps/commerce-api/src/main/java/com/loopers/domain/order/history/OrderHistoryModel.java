package com.loopers.domain.order.history;


import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_history")
@Getter
public class OrderHistoryModel extends BaseEntity {
  @Column(nullable = false)
  private Long orderId;
  @Column(nullable = false, length = 25)
  private String orderNumber;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false, length = 25)
  private String status;

  @Column(nullable = false, length = 200)
  private String address;

  @Column(columnDefinition = "TEXT")
  private String memo;

  @Builder(builderMethodName = "create")
  public OrderHistoryModel(Long orderId, String orderNumber, String userId, String status, String address,
                           BigInteger usePoint,
                           String memo) {

    if(orderId == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문 내역에는 주문아이디기 필수입니다.");
    }

    if(orderNumber == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문 내역에는 주문번호가 필수입니다.");
    }

    if(userId == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문 내역에는 계정아이디가 필수입니다.");
    }
    if(status == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문 내역에는 계정 상태가 필수입니다.");
    }
    if(address == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문 내역에는 주소가 필수입니다.");
    }




    this.orderId = orderId;
    this.orderNumber = orderNumber;
    this.userId = userId;
    this.status = status;
    this.address = address;
    this.memo = memo;
  }
}
