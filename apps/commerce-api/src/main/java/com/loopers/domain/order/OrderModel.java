package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.embeded.OrderItems;
import com.loopers.domain.order.embeded.OrderNumber;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "orders")
@Getter
@ToString
public class OrderModel extends BaseEntity {

  @Embedded
  private OrderNumber orderNumber;

  private String userId;

  private BigInteger totalPrice;

  @Embedded
  private OrderItems orderItems;

  @Column(length = 200)
  private String address;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;


  // 할인 금액
  private BigInteger discountPrice;

  @Column(columnDefinition = "TEXT")
  private String memo;


  @Builder(builderMethodName = "create")
  public OrderModel(String userId, String address,
                    String memo) {

    if (userId == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주문자가 존재하지 않는 경우, 주문서를 작성할 수 없습니다.");
    }

    if (address == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "주소가 존재하지 않는 경우, 주문서를 작성할 수 없습니다.");
    }

    this.orderNumber = new OrderNumber();
    this.userId = userId;
    this.orderItems = new OrderItems();
    this.totalPrice = BigInteger.ZERO;
    this.address = address;
    this.status = OrderStatus.ORDER;
    this.discountPrice = BigInteger.ZERO; // 할인 금액은 초기에는 0으로 설정
    this.memo = memo;
  }

  public void addOrderItemsAfterSave(List<OrderItemModel> orderItems) {
    this.orderItems.addAll(orderItems);
    this.totalPrice = sumPrice(); // 총액 재계산
  }

  public BigInteger sumPrice() {
    this.totalPrice = orderItems.sum();
    return totalPrice;
  }


  public String getOrderNumber() {
    return this.orderNumber.getNumber();
  }

  public List<OrderItemModel> getOrderItems() {
    return this.orderItems.getOrderItems();
  }

  public void cancel() {

    if (status != OrderStatus.ORDER) {
      throw new IllegalArgumentException("주문 상태가 아닌 주문은 취소할 수 없습니다.");
    }

    this.status = OrderStatus.CANCEL;
  }

  public void done() {
    this.status = OrderStatus.DONE;
  }

  public void forceChange(String state) {
    this.status = OrderStatus.valueOf(state);
  }

  public void paymentCheck() {
    if (status != OrderStatus.ORDER) {
      throw new IllegalArgumentException("주문 상태가 아닌 결제는 불가합니다.");
    }
  }

  public void addDiscountValue(BigInteger discount) {
    this.discountPrice = discount;
  }
}

