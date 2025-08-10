package com.loopers.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.application.order.command.OrderCreateCommand;
import com.loopers.application.order.command.OrderItemCommands;
import com.loopers.application.order.info.OrderCancelInfo;
import com.loopers.application.order.info.OrderCreateInfo;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/test-fixture.sql")
class OrderServiceIntegrationTest {
  @Autowired
  private OrderFacade orderFacade;

  @Autowired
  private OrderJpaRepository repository;

  @Autowired
  private PointRepository pointRepository;
  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("주문 생성 시 주문 정보가 반환된다")
  @Test
  void returnOrderInfo_whenCreateOrder() {
    //given
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 1L
    ));

    orderItemModels.add(new OrderItemCommands(
        2L, 2L
    ));

    OrderCreateCommand command =
        new OrderCreateCommand("userId",
            "서울시 송파구"
            , orderItemModels, "메모..");
    //when
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);

    OrderModel dbModel = repository.findById(orderCreateInfo.orderId()).get();
    //then
    assertAll(
        () -> assertThat(orderCreateInfo.orderStatus()).isEqualTo(dbModel.getStatus().name()),
        () -> assertThat(orderCreateInfo.orderId()).isEqualTo(dbModel.getId()),
        () -> assertThat(orderCreateInfo.userId()).isEqualTo(dbModel.getUserId()),
        () -> assertThat(orderCreateInfo.totalPrice()).isEqualTo(dbModel.getTotalPrice()),
        () -> assertThat(orderCreateInfo.memo()).isEqualTo(dbModel.getMemo())
    );

    System.out.println(orderCreateInfo);

  }


  @DisplayName("주문 취소 시, 권한이 없는 주문을 취소하는 경우 `409 CONFLICT`가 반환된다.")
  @Test
  void throw409Conflict_whenNotAuthorityIsCancel() {
    //given
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 1L
    ));

    orderItemModels.add(new OrderItemCommands(
        2L, 2L
    ));

    String order = "userId"; //주문자
    OrderCreateCommand command =
        new OrderCreateCommand(order,
            "서울시 송파구"
            , orderItemModels, "메모..");
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);
    //when
    CoreException result = assertThrows(CoreException.class, () -> orderFacade.cancel("userId2", orderCreateInfo.orderNumber()));
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.CONFLICT);

  }

  @DisplayName("주문 취소 시, 취소 결과가 반환된다.")
  @Test
  void returnCancelState_whenOrderCancel() {
    //given
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 1L
    ));

    orderItemModels.add(new OrderItemCommands(
        2L, 2L
    ));

    String order = "userId"; //주문자
    OrderCreateCommand command =
        new OrderCreateCommand(order,
            "서울시 송파구"
            , orderItemModels, "메모..");
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);
    //when
    OrderCancelInfo cancel = orderFacade.cancel(order, orderCreateInfo.orderNumber());
    //then
    assertThat(cancel.orderStaus()).isEqualTo("CANCEL");
  }

  @DisplayName("주문시 포인트 확인,")
  @Nested
  class Point {

    @DisplayName("주문 시 유저의 포인트 잔액이 부족할 경우 `400 Bad Request`를 반환한다.")
    @Test
    void throw400_whenUserHasLessPointsThanOrderRequires() {
      //given
      List<OrderItemCommands> orderItemModels = new ArrayList<>();

      orderItemModels.add(new OrderItemCommands(
          1L, 1L
      ));

      OrderCreateCommand command =
          new OrderCreateCommand("userId",
              "서울시 송파구"
              , orderItemModels, "메모..");

      pointRepository.save(new PointModel("userId", BigInteger.valueOf(1)));

      //when
      CoreException result = assertThrows(CoreException.class, () -> orderFacade.create(command));
      //then
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

    }
  }

  @DisplayName("주문시, 재고 확인")
  @Nested
  class Stock {

    @DisplayName("주문 시 재고가 부족할 경우 `400 Bad Request`를 반환한다.")
    @Test
    void throw400_whenCreatingOrderWithInsufficientStock() {
      //given
      List<OrderItemCommands> orderItemModels = new ArrayList<>();

      orderItemModels.add(new OrderItemCommands(
          1L, 3000L
      ));

      OrderCreateCommand command =
          new OrderCreateCommand("userId",
              "서울시 송파구"
              , orderItemModels, "메모..");

      pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));

      //when
      CoreException result = assertThrows(CoreException.class, () -> orderFacade.create(command));
      //then
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

    }

  }

}
