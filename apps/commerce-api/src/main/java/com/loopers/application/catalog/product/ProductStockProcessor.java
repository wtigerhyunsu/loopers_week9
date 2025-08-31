package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.stock.StockDecreaseCommand;
import com.loopers.domain.order.orderItem.OrderItemModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockProcessor {
  private final StockProcessor stockProcessor;


  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void decrease(StockDecreaseCommand command) {
    log.info("재고 차감 이벤트 수신: {}", command);
    for (OrderItemModel orderItem : command.orderItems()) {
      Long productId = orderItem.getProductId();
      Long quantity = orderItem.getQuantity();
      stockProcessor.decreaseStock(productId, quantity);
    }
  }
}
