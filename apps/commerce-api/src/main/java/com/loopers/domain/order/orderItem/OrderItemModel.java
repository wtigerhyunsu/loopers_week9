package com.loopers.domain.order.orderItem;

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
@Table(name = "order_item")
@Getter
public class OrderItemModel extends BaseEntity {

  @Column(name = "owner_order_id", nullable = false)
  private Long orderId;
  @Column(nullable = false)
  private Long productId;
  @Column(nullable = false)
  private Long quantity;
  private BigInteger unitPrice;

  @Builder
  public OrderItemModel(Long orderId, Long productId, Long quantity, BigInteger unitPrice) {
    if(orderId == null) {
      throw new CoreException(ErrorType.NOT_FOUND,"주문 아이템은 주문ID가 필요합니다");
    }
    if(productId == null) {
      throw new CoreException(ErrorType.NOT_FOUND,"주문 아이템은 상품ID가 필요합니다");
    }

    if(quantity == null || quantity <= 0) {
      throw new CoreException(ErrorType.BAD_REQUEST,"수량은 0이하거나 없을 수 없습니다.");
    }

    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.unitPrice = unitPrice;

    // unitPrice가 존재하지 않는 경우 0을 리턴합니다.
    if(unitPrice == null) {
      this.unitPrice = BigInteger.ZERO;
    }
  }

  public void addOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public void plusQuantity(Long quantity) {
    this.quantity += quantity;
  }
}
