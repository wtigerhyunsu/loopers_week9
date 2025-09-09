package com.loopers.application.metrics;

import com.loopers.domain.event.EventHandledRepository;
import com.loopers.domain.metrics.MetricsRepository;
import com.loopers.domain.metrics.SalesMetricsMessage;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConvert;
import org.springframework.stereotype.Component;

@Component
public class MetricsSalesStrategy implements MetricsStrategy {
  private final MessageConvert convert;
  private final EventHandledRepository eventHandledRepository;
  private final MetricsRepository repository;

  public MetricsSalesStrategy(MessageConvert convert, EventHandledRepository eventHandledRepository, MetricsRepository repository) {
    this.convert = convert;
    this.eventHandledRepository = eventHandledRepository;
    this.repository = repository;
  }

  @Override
  public void process(String message) {
    Message convertMessage = convert.convert(message, Message.class);
    SalesMetricsMessage result = convert.convert(convertMessage.getPayload(), SalesMetricsMessage.class);
    repository.upsertSales(result.productId(), result.quantity());
    eventHandledRepository.save(convertMessage.getEventId());
  }

  @Override
  public MetricsMethod method() {
    return MetricsMethod.SALES;
  }
}
