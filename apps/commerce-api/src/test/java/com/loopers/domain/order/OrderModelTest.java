package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.order.embeded.OrderItems;
import com.loopers.domain.order.embeded.OrderNumber;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderModelTest {

  @DisplayName("주문번호는 날짜 + UUID 앞을 따서 생성합니다.")
  @Test
  void returnOrderNumberIsDateAndUuid_whenGenerate() {
    //YYYYMMDD-UUID
    //given
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String today = formatter.format(now);
    //when
    OrderNumber orderNumber = new OrderNumber();
    String uuidPart = orderNumber.getNumber().substring(today.length() + 1); // '-' 다음 부분
    //then
    assertAll(
        () -> assertThat(orderNumber.getNumber()).startsWith(today + "-"),
        () -> assertThat(uuidPart).matches("[0-9a-fA-F]{8}"),
        () -> assertThat(orderNumber.getNumber().length()).isEqualTo(today.length() + 1 + 8));

  }

  @DisplayName("주문 상품을,")
  @Nested
  class OrderItem {
    // 한개만 등록하는 경우
    @DisplayName("한개만 등록하는 경우, 주문 상품의 갯수는 1를 리턴합니다.")
    @Test
    void returnOrderItemSizeIs1_whenRegisterOneProduct() {
      //given
      OrderItemModel orderItem = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();
      //when
      orderItems.add(orderItem);
      //then
      assertThat(orderItems.size()).isEqualTo(1);
    }

    // 여러개를 등록하는 경우
    @DisplayName("여려개를 등록하는 경우, 주문 상품의 갯수는 그 수 만큼 리턴합니다.")
    @Test
    void returnOrderItemSizeIsN_whenRegisterNProduct() {
      //given
      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(2L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();

      List<OrderItemModel> orderItemList = Arrays.asList(orderItem1, orderItem2);
      //when
      orderItems.addAll(orderItemList);
      //then
      assertThat(orderItems.size()).isEqualTo(2);
    }

    // 한개를 제거 하는 경우
    @DisplayName("두개에서 한개를 제거하는 경우, 주문 상품의 갯수는 한 개를 리턴합니다.")
    @Test
    void returnOrderItemSizeIs1_whenRemove1Product() {
      //given
      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(2L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();

      List<OrderItemModel> orderItemList = Arrays.asList(orderItem1, orderItem2);
      orderItems.addAll(orderItemList);
      //when
      orderItems.remove(1L);
      //then
      assertThat(orderItems.size()).isEqualTo(1);
    }

    // 여러개를 제거 하는 경우
    @DisplayName("두개에서 두개를 제거하는 경우, 주문 상품의 갯수는 0 개를 리턴합니다.")
    @Test
    void returnOrderItemSizeIs0_whenRemove2Product() {
      //given
      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(2L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();

      List<OrderItemModel> orderItemList = Arrays.asList(orderItem1, orderItem2);
      orderItems.addAll(orderItemList);
      //when
      orderItems.removeAll(List.of(1L, 2L));
      //then
      assertThat(orderItems.size()).isEqualTo(0);
    }

    // 존재하지 않는 상품을 제거 하는 경우
    @DisplayName("존재하지 않는 상품을 제거하는 경우, `IllegalArgumentException`를 반환한다.")
    @Test
    void throwIllegalArgumentException_whenDeletedNotExitsProduct() {
      //given
      OrderItemModel orderItem = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();

      orderItems.add(orderItem);
      //when&then
      Exception result = assertThrows(IllegalArgumentException.class, () -> orderItems.remove(2L));
    }

    // 존재하지 않는 상품을 여러개 제거하는 경우
    @DisplayName("존재하지 않는 상품 여러개를 제거하는 경우, `IllegalArgumentException`를 반환한다.")
    @Test
    void throwIllegalArgumentException_whenDeletedNotExitsMultiProduct() {
      //given
      OrderItemModel orderItem = OrderItemModel.builder()
          .orderId(1L)
          .productId(1L)
          .quantity(3L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();

      orderItems.add(orderItem);
      //when&then
      Exception result = assertThrows(IllegalArgumentException.class, () -> orderItems.removeAll(List.of(2L, 3L)));
    }

    // 이미 등록된 상품을 등록하는 경우
    @DisplayName("이미 등록된것을 재 등록하는 경우, 주문 상품의 갯수는 1를 리턴하고 갯수는 합쳐집니다.")
    @Test
    void returnOrderItemSizeIs1AndSumTotalQuantity_whenAlreadyOneProduct() {
      //given
      Long productId = 1L;
      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(5L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(4L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem3 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(4L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();
      //when
      orderItems.add(orderItem1);
      orderItems.add(orderItem2);
      orderItems.add(orderItem3);
      //then
      assertThat(orderItems.size()).isEqualTo(1);
      assertThat(orderItems.findQuantity(1L)).isEqualTo(13L);
    }

    // 이미 등록된 상품을 여러개 등록하는 경우
    @DisplayName("이미 등록된것을 여러개 재 등록하는 경우, 주문 상품의 갯수는 2를 리턴하고 갯수는 합쳐집니다.")
    @Test
    void returnOrderItemSizeIs2AndSumTotalQuantity_whenAlreadyMultiProduct() {
      //given
      Long productId = 1L;
      Long productId2 = 2L;

      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(1L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId2)
          .quantity(8L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem3 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(9L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItems orderItems = new OrderItems();
      //when
      orderItems.addAll(List.of(orderItem1, orderItem2, orderItem3));
      //then
      assertThat(orderItems.size()).isEqualTo(2);
      assertThat(orderItems.findQuantity(1L)).isEqualTo(10L);
      assertThat(orderItems.findQuantity(2L)).isEqualTo(8L);
    }

    // 아무상품도 등록하지 않았을때, 총 상품 가격
    @DisplayName("등록하지 않는 경우, 총 상품 가격을 0원을 리턴합니다.")
    @Test
    void returnOrderPriceIsZero_whenRegisterEmptyProduct() {
      //given
      OrderItems orderItems = new OrderItems();
      //when
      BigInteger sum = orderItems.sum();
      //then
      assertThat(sum).isEqualTo(0);
    }

    // N개를 등록했을때, 총 상품 가격
    @DisplayName("여러개 등록한 경우, 총 상품 가격을 (등록상품 * quantity)를 리턴합니다.")
    @Test
    void returnOrderPrice_whenRegisterManyProduct() {
      //given
      Long productId = 1L;
      Long productId2 = 2L;

      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(1L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId2)
          .quantity(8L)
          .unitPrice(BigInteger.valueOf(1000))
          .build();

      OrderItemModel orderItem3 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(9L)
          .unitPrice(BigInteger.valueOf(100))
          .build();
      OrderItems orderItems = new OrderItems();
      orderItems.addAll(List.of(orderItem1, orderItem2, orderItem3));

      //when
      BigInteger sum = orderItems.sum();
      //then
      assertThat(sum).isEqualTo(9000);
    }
  }

  @DisplayName("주문을 생성하는 경우,")
  @Nested
  class Create {

    @DisplayName("주문자가 존재하지 않는 경우, `404 Not Found`를 리턴한다.")
    @Test
    void throws404NotFound_whenNotExitsOrder_asUserId() {
      //given
      String address = "서울시 송파구";
      String memo = "문앞에 두세요";

      //when
      CoreException result = assertThrows(CoreException.class, () -> OrderModel.create()
          .userId(null)
          .address(address)
          .memo(memo)
          .build());

      //then
      assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("주소가 존재하지 않는 경우, `404 Not Found`를 리턴한다.")
    @Test
    void throws404NotFound_whenNotExitsAddress() {
      //given
      String userId = "userId";
      String address = null;
      String memo = "문앞에 두세요";
      //when
      CoreException result = assertThrows(CoreException.class, () -> OrderModel.create()
          .userId(userId)
          .address(address)
          .memo(memo)
          .build());
      //then
      assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("주문이 성공해한 경우, 주문 객체를 리턴한다.")
    @Test
    void returnOrderInfo_whenCreateOrder() {
      //given
      Long productId = 1L;
      Long productId2 = 2L;

      OrderItemModel orderItem1 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(1L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      OrderItemModel orderItem2 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId2)
          .quantity(8L)
          .unitPrice(BigInteger.valueOf(1000))
          .build();

      OrderItemModel orderItem3 = OrderItemModel.builder()
          .orderId(1L)
          .productId(productId)
          .quantity(9L)
          .unitPrice(BigInteger.valueOf(100))
          .build();

      List<OrderItemModel> orderItems = List.of(orderItem1, orderItem2, orderItem3);

      String userId = "userId";
      String address = "서울시 송파구";
      String memo = "문앞에 두세요";

      //when
      OrderModel orderModel = OrderModel.create()
          .userId(userId)
          .address(address)
          .memo(memo)
          .build();

      orderModel.addOrderItemsAfterSave(orderItems);

      //then
      assertAll(() -> assertThat(orderModel.getUserId()).isEqualTo(userId),
          () -> assertThat(orderModel.getTotalPrice()).isEqualTo(9000),
          () -> assertThat(orderModel.getStatus()).isEqualTo(OrderStatus.ORDER),
          () -> assertThat(orderModel.getAddress()).isEqualTo(address),
          () -> assertThat(orderModel.getMemo()).isEqualTo(memo));

    }
  }

  @DisplayName("주문상태가 주문이 아닌 경우, `IllegalArgumentException`를 반환합니다.")
  @ParameterizedTest
  @ValueSource(strings = {"CANCEL", "DONE"})
  void throwIllegalArgumentException_whenOderStateIsNotOrder(String state) {
    // given
    OrderModel orderModel = OrderModel.create().userId("userId")
        .address("주소")
        .memo("메모").build();
    orderModel.forceChange(state);
    //when&then
    Exception result = assertThrows(IllegalArgumentException.class, orderModel::cancel);
  }

  // 주문을 취소하는 경우
  @DisplayName("주문을 취소하는 경우, 주문상태를 CANCEL로 변경합니다.")
  @Test
  void changeOrderStatus_whenCancelOrder() {
    // given
    OrderModel orderModel = OrderModel.create().userId("userId")
        .address("주소")
        .memo("메모").build();

    //when
    orderModel.cancel();
    //then
    assertThat(orderModel.getStatus()).isEqualTo(OrderStatus.CANCEL);
  }

  // 주문이 완료가 되어지는 경우
  @DisplayName("주문이 완료가 되어지는 경우(결제까지 완료), 주문 상태를 DONE으로 변경합니다.")
  @Test
  void changeOrderStatus_whenDoneOrder() {
    // given
    OrderModel orderModel = OrderModel.create().userId("userId")
        .address("주소")
        .memo("메모").build();

    //when
    orderModel.done();
    //then
    assertThat(orderModel.getStatus()).isEqualTo(OrderStatus.DONE);
  }
}
