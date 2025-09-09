package com.loopers.infrastructure.catalog.product;

import com.loopers.application.catalog.product.ProductPublisher;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConverter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaPublisher implements ProductPublisher {
  private final KafkaTemplate<Object, Object> kafkaAtLeastTemplate;
  private final MessageConverter messageConverter;
  private final static String AGGREGATE_TOPIC = "PRODUCT_VIEWS_CHANGED_V1";

  @Override
  public void aggregate(Long productId) {
    String message = messageConverter.convert(new Message(String.valueOf(productId)));
    String key = LocalDate.now().toEpochDay() + ":" + productId;
    kafkaAtLeastTemplate.send(AGGREGATE_TOPIC, key, message);
  }
}
