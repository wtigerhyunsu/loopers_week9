package com.loopers.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.loopers.domain.coupon.CouponModel;
import com.loopers.domain.coupon.issued.IssuedCouponRepository;
import com.loopers.domain.order.OrderCouponRegisterCommand;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.infrastructure.coupon.CouponJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
  private CouponJpaRepository couponJpaRepository;

  @Autowired
  private IssuedCouponRepository issuedCouponRepository;


  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @MockitoBean
  private CouponProcessor processor;

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
            "서울시 송파구",
            orderItemModels, null, "메모..");
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

  @DisplayName("주문 생성 시, 쿠폰이 사용이 되어지면, 주문 정보가 반환된다")
  @Test
  void returnOrderInfo_whenCreateOrderAddCoupon() {
    //given
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 1L
    ));

    orderItemModels.add(new OrderItemCommands(
        2L, 2L
    ));

    CouponModel coupon = couponJpaRepository.save(new CouponModel("FIXED", 100, 10, "설명"));

    OrderCreateCommand command =
        new OrderCreateCommand("userId",
            "서울시 송파구",
            orderItemModels, coupon.getId(), "메모..");
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    //when
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);

    CouponModel afterCoupon = couponJpaRepository.findById(coupon.getId()).get();

    //then
    assertThat(issuedCouponRepository.exists("userId", orderCreateInfo.orderId(), coupon.getId())).isTrue();
    assertThat(afterCoupon.getCount()).isEqualTo(coupon.getCount() - 1);
    assertThat(orderCreateInfo.discountPrice()).isEqualTo(orderCreateInfo.totalPrice().subtract(BigInteger.valueOf(coupon.getDiscountValue())));

  }


  @DisplayName("쿠폰 등록시, 예외가 발생하면 쿠폰은 생성되지 않는다.")
  @Test
  void returnOrderInfo_whenThrows_thenNotCreatedCoupon() {
    //given
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 1L
    ));

    orderItemModels.add(new OrderItemCommands(
        2L, 2L
    ));

    CouponModel coupon = couponJpaRepository.save(new CouponModel("FIXED", 100, 10, "설명"));

    OrderCreateCommand command =
        new OrderCreateCommand("userId",
            "서울시 송파구",
            orderItemModels, coupon.getId(), "메모..");
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    //when
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);

    doThrow(new RuntimeException("강제 예외")).when(processor)
        .register(any(OrderCouponRegisterCommand.class));

    CouponModel afterCoupon = couponJpaRepository.findById(coupon.getId()).get();

    //then
    assertThat(issuedCouponRepository.exists("userId", orderCreateInfo.orderId(), coupon.getId())).isFalse();
    assertThat(afterCoupon.getCount()).isEqualTo(coupon.getCount());

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
            "서울시 송파구",
            orderItemModels, null, "메모..");
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
            "서울시 송파구",
            orderItemModels, null, "메모..");
    pointRepository.save(new PointModel("userId", BigInteger.valueOf(500000000)));
    OrderCreateInfo orderCreateInfo = orderFacade.create(command);
    //when
    OrderCancelInfo cancel = orderFacade.cancel(order, orderCreateInfo.orderNumber());
    //then
    assertThat(cancel.orderStaus()).isEqualTo("CANCEL");
  }


}
