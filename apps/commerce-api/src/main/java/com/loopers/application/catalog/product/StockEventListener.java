package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.stock.StockDecreaseEvent;
import com.loopers.domain.catalog.product.stock.StockMetricsMessage;
import com.loopers.domain.catalog.product.stock.StockPublisher;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final StockCacheRepository repository;
  private final MessageConverter converter;
  private final StockPublisher stockPublisher;

  @Async
  @EventListener
  public void decrease(StockDecreaseEvent event) {
    Long cacheCurrent = repository.get(event.productId());
    // 캐시가 존재하지 않는 경우
    if (cacheCurrent == 0L) {
      repository.init(event.productId(), event.current() - event.quantity());
      return;
    }
    repository.decrease(event.productId(), event.quantity());
    //집계

    Message message = new Message(converter.convert(new StockMetricsMessage(event.productId(), event.quantity())));

    stockPublisher.aggregate(message, event.productId());
  }
}
