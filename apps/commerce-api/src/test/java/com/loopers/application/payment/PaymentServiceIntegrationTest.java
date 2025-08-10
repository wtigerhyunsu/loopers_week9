package com.loopers.application.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.command.OrderCreateCommand;
import com.loopers.application.order.command.OrderItemCommands;
import com.loopers.application.order.info.ItemInfos;
import com.loopers.application.order.info.OrderCreateInfo;
import com.loopers.application.payment.command.PaymentCommand;
import com.loopers.application.point.PointFacade;
import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.domain.catalog.product.stock.StockRepository;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.infrastructure.catalog.product.stock.StockJpaRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/test-fixture.sql")
class PaymentServiceIntegrationTest {

  @Autowired
  private PaymentFacade paymentFacade;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private OrderFacade orderFacade;

  @Autowired
  private PointRepository pointRepository;
  @Autowired
  private PointJpaRepository pointJpaRepository;

  @Autowired
  private PointFacade pointFacade;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private StockJpaRepository stockJpaRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  private OrderCreateInfo orderCreateInfo;
  private String userId;

  @BeforeEach
  void init() {
    List<OrderItemCommands> orderItemModels = new ArrayList<>();

    orderItemModels.add(new OrderItemCommands(
        1L, 10L
    ));
    userId = "userId";
    OrderCreateCommand command =
        new OrderCreateCommand(userId,
            "서울시 송파구"
            , orderItemModels, "메모..");

    pointRepository.save(new PointModel(userId, BigInteger.valueOf(50000000)));

    orderCreateInfo = orderFacade.create(command);

    UserModel userModel = userRepository.save(
        UserModel.create()
            .userId(userId)
            .email("email@eami.com")
            .birthday("2022-01-01")
            .gender("M")
            .build()
    );
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("결제 생성 시, 포인트가 차감이 되어진다.")
  @Test
  void returnDecreasePoint_whenPaymentCreated() {
    //given
    String orderNumber = orderCreateInfo.orderNumber();
    PointModel afterPoint = pointJpaRepository.findByUserId(userId).get();
    BigInteger totalPrice = BigInteger.valueOf(100000);
    PaymentCommand command = new PaymentCommand(userId, orderNumber, totalPrice, "shot");
    //when
    PaymentInfo payment = paymentFacade.payment(command);
    PointModel currentPoint = pointJpaRepository.findByUserId(userId).get();
    //then
    assertThat(currentPoint.getPoint()).isEqualTo(
        afterPoint.getPoint().subtract(totalPrice));
    System.out.println(payment);
  }

  @DisplayName("결재 생성 시, 재고가 차감이 되어진다.")
  @Test
  void returnDecreasedStockQuantity_whenPaymentCreated() {
    //given
    StockModel afterStock = stockJpaRepository.findByProductId(1L).get();
    pointFacade.charge(userId, BigInteger.valueOf(500000));
    PaymentCommand command = new PaymentCommand(userId, orderCreateInfo.orderNumber(), orderCreateInfo.totalPrice(), "shot");
    //when
    //결제시
    paymentFacade.payment(command);

    StockModel currentStock = stockJpaRepository.findByProductId(1L).get();
    //then
    assertThat(currentStock.stock()).isEqualTo(afterStock.stock() - 10L);
  }

  @DisplayName("결제 롤백 테스트,")
  @Nested
  class Rollback {
    @DisplayName("포인트가 부족한 경우, 결제가 생성되지 않는다. (재고가 감소하지 않는다.)")
    @Test
    void shouldRollbackPayment_whenPointIsInsufficient() {
      //given
      String orderNumber = orderCreateInfo.orderNumber();
      List<Long> orderProducts = orderCreateInfo.items()
          .stream().map(ItemInfos::productId).toList();
      Map<Long, Long> originProductStock = stockJpaRepository.findAll()
          .stream().filter(
              stock -> orderProducts.contains(stock.getProductId())
          )
          .collect(Collectors.toMap(
              StockModel::getProductId,
              StockModel::stock
          ));

      // 포인트 1로 변환
      pointJpaRepository.deleteByUserIdForTest(userId);
      PointModel model = pointRepository.save(new PointModel(userId, BigInteger.valueOf(1)));

      PaymentCommand payment = new PaymentCommand(
          userId,
          orderNumber,
          BigInteger.valueOf(30000),
          "설명"
      );

      //when
      assertThatThrownBy(() -> paymentFacade.payment(payment));
      Optional<PaymentModel> paymentModel = paymentRepository.get(orderNumber);

      Map<Long, Long> currentProductStock = stockJpaRepository.findAll()
          .stream().filter(
              stock -> orderProducts.contains(stock.getProductId())
          )
          .collect(Collectors.toMap(
              StockModel::getProductId,
              StockModel::stock
          ));

      //then

      //주문 상품은 생성되지 않아야 한다.
      assertThat(paymentModel).isEmpty();

      // 재고 확인 재고는 변경되지 않는다.
      for (Entry<Long, Long> entry : originProductStock.entrySet()) {
        assertThat(currentProductStock.get(entry.getKey())).isEqualTo(entry.getValue());
      }
    }


    @DisplayName("재고가 부족한 경우, 결제가 생성되지 않는다. (포인트는 감소하지 않는다.)")
    @Test
    void shouldRollbackPayment_whenStocksIsInsufficient() {
      //given
      String orderNumber = orderCreateInfo.orderNumber();
      // 기존 포인트
      PointModel hasPoint = pointJpaRepository.findByUserId(userId).get();
      // 모든 재고는 재거한다.
      stockJpaRepository.deleteByProductIdForTest(1L);
      stockJpaRepository.save(new StockModel(1L, 1L));

      PaymentCommand payment = new PaymentCommand(
          userId,
          orderNumber,
          BigInteger.valueOf(30000),
          "설명"
      );

      //when
      assertThatThrownBy(() -> paymentFacade.payment(payment));
      PointModel currentPoint = pointJpaRepository.findByUserId(userId).get();
      Optional<PaymentModel> paymentModel = paymentRepository.get(orderNumber);

      //then
      //주문 상품은 생성되지 않아야 한다.
      assertThat(paymentModel).isEmpty();
      //포인트는 감소하지 않는다.
      assertThat(hasPoint.getPoint()).isEqualTo(currentPoint.getPoint());

    }
  }

}
