package com.loopers.interfaces.consumer;

import com.loopers.application.evict.EvictService;
import com.loopers.config.kafka.KafkaConfig;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class EvictKafkaConsumer {
  private final EvictService service;

  public EvictKafkaConsumer(EvictService service) {
    this.service = service;
  }

  @KafkaListener(
      topics = {"${evict-kafka.like.topic-name}"
          , "${evict-kafka.stock.topic-name}"
      },
      groupId = "${evict-kafka.group-id}",
      containerFactory = KafkaConfig.BATCH_LISTENER
  )
  public void evictListener(
      List<ConsumerRecord<String, String>> messages,
      Acknowledgment acknowledgment
  ) {
    for (ConsumerRecord<String, String> message : messages) {
      service.evict(message.value());
    }

    acknowledgment.acknowledge();
  }
}
