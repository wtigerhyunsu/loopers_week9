package com.loopers.infrastructure.catalog.product.stock;

import com.loopers.application.catalog.product.StockEventPublisher;
import com.loopers.domain.catalog.product.stock.StockDecreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockEventCoreEventPublisher implements StockEventPublisher {
  private final ApplicationEventPublisher event;

  @Override
  public void decrease(Long productId, Long quantity, Long current) {
    event.publishEvent(new StockDecreaseEvent(productId, quantity, current));
  }
}
