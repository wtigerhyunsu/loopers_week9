package com.loopers.application.payment;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.loopers.application.popularity.PopularityService;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.infrastructure.payment.PaymentOrderProcessor;
import java.util.List;
import org.junit.jupiter.api.Test;

class PaymentFacadePopularityTest {

  @Test
  void increments_popularity_on_success_callback() {
    PaymentProcessor paymentProcessor = mock(PaymentProcessor.class);
    PaymentHistoryProcessor paymentHistoryProcessor = mock(PaymentHistoryProcessor.class);
    PaymentStrategyFactory paymentFactory = mock(PaymentStrategyFactory.class);
    PaymentPublisher publisher = mock(PaymentPublisher.class);
    PaymentOrderProcessor orderProcessor = mock(PaymentOrderProcessor.class);
    PopularityService popularityService = mock(PopularityService.class);

    PaymentFacade facade = new PaymentFacade(
        paymentProcessor,
        paymentHistoryProcessor,
        paymentFactory,
        publisher,
        orderProcessor,
        popularityService
    );

    PaymentModel paymentModel = mock(PaymentModel.class);
    when(paymentProcessor.get("tx1")).thenReturn(paymentModel);

    OrderModel orderModel = mock(OrderModel.class);
    OrderItemModel item = mock(OrderItemModel.class);
    when(item.getProductId()).thenReturn(100L);
    when(item.getQuantity()).thenReturn(3L);
    when(item.getUnitPrice()).thenReturn(java.math.BigInteger.valueOf(5000));
    when(orderModel.getOrderItems()).thenReturn(List.of(item));
    when(orderProcessor.get("ord1")).thenReturn(orderModel);

    PaymentCallBackCommand cmd = new PaymentCallBackCommand("tx1", "ord1", 1000L, "SUCCESS", null);

    facade.callback(cmd);

    verify(popularityService).incrementPurchase(eq(100L), eq(3L), eq(15000L));
  }
}
