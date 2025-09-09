package com.loopers.infrastructure.catalog.product.stock;

import com.loopers.domain.catalog.product.stock.StockEvictMessage;
import com.loopers.domain.catalog.product.stock.StockPublisher;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConverter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockKafkaPublisher implements StockPublisher {
  private final KafkaTemplate<Object, Object> kafkaAtLeastTemplate;
  private final MessageConverter converter;
  private final static String EVICT_TOPIC = "PRODUCT_STOCK_EVICT_V1";
  private final static String AGGREGATE_TOPIC = "PRODUCT_STOCK_CHANGED_V1";

  @Override
  public void aggregate(@Payload Message message, Long productId) {
    log.info("카프카를 통해 재고정보가 전송이 되었습니다.");


    String key = LocalDate.now().toEpochDay() + ":" + productId;
    kafkaAtLeastTemplate.send(AGGREGATE_TOPIC, key, message);
  }

  @Override
  public void evict(Long productId) {
    log.info("카프카를 통해 재고정보 캐시 삭제가 요청 되었습니다.");

    String message = converter.convert(
        new Message(
            converter.convert(new StockEvictMessage(productId))
        )
    );

    kafkaAtLeastTemplate.send(EVICT_TOPIC, String.valueOf(productId), message);
  }

  @Override
  public void aggregate(Long productId, Long quantity) {
    return;
  }


}
