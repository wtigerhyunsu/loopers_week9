package com.loopers.interfaces.consumer;

import com.loopers.application.metrics.MetricsMethod;
import com.loopers.application.metrics.MetricsStrategyFactory;
import com.loopers.config.kafka.KafkaConfig;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class MetricsKafkaConsumer {
  private final MetricsStrategyFactory factory;
  public MetricsKafkaConsumer(MetricsStrategyFactory factory) {
    this.factory = factory;
  }

  @KafkaListener(
      topics = {
          "${aggregate-kafka.stock.topic-name}",
          "${aggregate-kafka.like.topic-name}",
          "${aggregate-kafka.views.topic-name}"
      },
      groupId = "${aggregate-kafka.group-id}",
      containerFactory = KafkaConfig.BATCH_LISTENER
  )
  public void aggregateLikesListener(
      List<ConsumerRecord<String, String>> messages,
      Acknowledgment acknowledgment
  ) {

    for (ConsumerRecord<String, String> message : messages) {
      factory.getStrategy(MetricsMethod.find(message.topic())).process(message.value());
    }

    acknowledgment.acknowledge();
  }

}
