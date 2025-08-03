package com.loopers.domain.order.embeded;

import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Embeddable
@Getter
public class OrderItems {

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "owner_order_id")
  private List<OrderItemModel> orderItems;

  @Transient
  private Long parentOrderId;

  public OrderItems() {
    this.orderItems = new ArrayList<>();
  }

  // 등록
  public void add(OrderItemModel orderItemModel) {
    List<OrderItemModel> currentOrderItems = this.orderItems.stream()
        .filter(orderItem -> orderItem.getId().equals(orderItemModel.getId())).toList();

    if (!currentOrderItems.isEmpty()) {
      OrderItemModel currentOrderItem = currentOrderItems.getFirst();
      currentOrderItem.plusQuantity(orderItemModel.getQuantity());
      return;
    }

    this.orderItems.add(orderItemModel);
  }

  // 대량 등록
  public void addAll(List<OrderItemModel> orderItems) {
    if (orderItems.isEmpty()) {
      return;
    }

    // 1. 전달된 orderItems를 productId 기준으로 quantity 합치기
    Map<Long, OrderItemModel> incomingMap = orderItems.stream()
        .collect(Collectors.toMap(OrderItemModel::getProductId, item -> item,
            (existing, incoming) -> {
              existing.plusQuantity(incoming.getQuantity());
              return existing;
            }));

    // 2. 기존 this.orderItems도 map으로 바꿔서 병합
    Map<Long, OrderItemModel> existingMap = this.orderItems.stream()
        .collect(Collectors.toMap(
            OrderItemModel::getProductId,
            item -> item,
            (existing, duplicate) -> {
              existing.plusQuantity(duplicate.getQuantity());
              return existing;
            }
        ));

    // 3. 합치기 (중복 key는 quantity 더함)
    for (Map.Entry<Long, OrderItemModel> entry : incomingMap.entrySet()) {
      existingMap.merge(entry.getKey(), entry.getValue(), (oldItem, newItem) -> {
        oldItem.plusQuantity(newItem.getQuantity());
        return oldItem;
      });
    }

    // 기존 데이터와 합치기
    this.orderItems = new ArrayList<>(existingMap.values());
  }

  //제거
  public void remove(Long productId) {
    long count = this.orderItems.stream().filter(orderItem -> orderItem.getOrderId().equals(productId))
        .count();
    if (count == 0) {
      throw new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 상품은 제거할 수 없습니다.");
    }

    this.orderItems.removeIf(orderItemModel -> orderItemModel.getProductId().equals(productId));
  }

  //대량 제거
  public void removeAll(List<Long> removeProductId) {
    List<Long> currentIdList = orderItems.stream().map(OrderItemModel::getProductId).toList();
    for (Long removeId : removeProductId) {
      if (!currentIdList.contains(removeId)) {
        throw new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 상품은 제거할 수 없습니다.");
      }

    }

    this.orderItems.removeIf(orderItemModel -> removeProductId.contains(orderItemModel.getProductId()));
  }

  // 합산
  public BigInteger sum() {
    return this.orderItems.stream().map(orderItem -> orderItem.getUnitPrice()
            .multiply(BigInteger.valueOf(orderItem.getQuantity())))
        .reduce(BigInteger.ZERO, BigInteger::add);
  }

  public int size() {
    return this.orderItems.size();
  }

  public long findQuantity(long productId) {
    OrderItemModel orderItemModel = orderItems.stream().filter(orderItem -> orderItem.getProductId().equals(productId))
        .findFirst().orElseThrow(
            () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품ID를 찾을 수 없습니다.")
        );

    return orderItemModel.getQuantity();
  }
}
