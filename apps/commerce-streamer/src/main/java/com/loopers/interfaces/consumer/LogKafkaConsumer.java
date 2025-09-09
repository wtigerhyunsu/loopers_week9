package com.loopers.interfaces.consumer;

import com.loopers.application.log.EventLogService;
import com.loopers.config.kafka.KafkaConfig;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class LogKafkaConsumer {
  private final EventLogService service;

  public LogKafkaConsumer(EventLogService service) {
    this.service = service;
  }

  @KafkaListener(
      topics = "${logging-kafka.like.topic-name}",
      groupId = "${logging-kafka.group-id}",
      containerFactory = KafkaConfig.BATCH_LISTENER
  )
  public void logListener(
      List<ConsumerRecord<String, String>> messages,
      Acknowledgment acknowledgment
  ) {

    acknowledgment.acknowledge();
    for (ConsumerRecord<String, String> message : messages) {
      service.insert(message.value());
    }

  }
}
