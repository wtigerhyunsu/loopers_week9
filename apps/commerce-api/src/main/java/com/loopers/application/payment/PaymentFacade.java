package com.loopers.application.payment;


import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.domain.catalog.product.stock.StockRepository;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;
  private final StockRepository stockRepository;
  private final PointRepository pointRepository;

  @Transactional
  public PaymentInfo payment(PaymentCommand command) {
    String orderNumber = command.orderNumber();
    OrderModel orderModel = orderRepository.ofOrderNumber(orderNumber);

    // 포인트 감소
    PointModel hasPoint = pointRepository.get(command.userId()).orElseThrow(
        () -> new CoreException(ErrorType.BAD_REQUEST, "사용할 수 있는 포인트가 존재하지 않습니다.")
    );
    hasPoint.use(command.payment());

    PaymentModel paymentModel = PaymentModel.create()
        .userId(command.userId())
        .orderNumber(orderNumber)
        .description(command.description())
        .orderAmount(command.payment())
        .paymentAmount(orderModel.getTotalPrice())
        .build();

    // 결제 처리
    PaymentModel payment = paymentRepository.save(paymentModel);
    
    // 재고 차감
    for (OrderItemModel orderItem : orderModel.getOrderItems()) {
      Long productId = orderItem.getProductId();
      StockModel stockModel = stockRepository.get(productId);
      stockModel.decrease(orderItem.getQuantity());
    }

    // 주문 완료
    orderModel.done();

    return PaymentInfo.builder()
        .userId(payment.getUserId())
        .orderNumber(payment.getOrderNumber())
        .orderPrice(payment.getOrderAmount())
        .paymentPrice(payment.getPaymentAmount())
        .description(payment.getDescription())
        .build();
  }
}
