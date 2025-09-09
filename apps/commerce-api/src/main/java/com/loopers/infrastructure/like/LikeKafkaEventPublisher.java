package com.loopers.infrastructure.like;

import com.loopers.application.like.LikePublisher;
import com.loopers.domain.like.LikeEvictMessage;
import com.loopers.domain.like.LikeMetricsMessage;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConverter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeKafkaEventPublisher implements LikePublisher {
  private final KafkaTemplate<Object, Object> kafkaAtLeastTemplate;

  private final MessageConverter converter;
  private final static String AGGREGATE_TOPIC = "PRODUCT_LIKE_CHANGED_V1";
  private final static String EVICT_TOPIC = "PRODUCT_LIKE_EVICT_V1";

  public void aggregate(Long productId, int data) {
    log.info(" productId: {}, data: {}", productId, data);
    String key = LocalDate.now().toEpochDay() + ":" + productId;
    String message = converter.convert(new Message(converter.convert(new LikeMetricsMessage(productId, data))));
    kafkaAtLeastTemplate.send(AGGREGATE_TOPIC, key, message);
  }


  public void evict(Long productId) {
    log.debug("카프카를 통해 {}의 좋아요 캐시 제거 요청을 하였습니다.", productId);
    String message = converter.convert(new Message(converter.convert(new LikeEvictMessage(productId))));
    kafkaAtLeastTemplate.send(EVICT_TOPIC, String.valueOf(productId), message);
  }
}
