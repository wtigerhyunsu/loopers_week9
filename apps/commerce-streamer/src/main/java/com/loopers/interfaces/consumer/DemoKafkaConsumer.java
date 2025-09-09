package com.loopers.interfaces.consumer;

import com.loopers.config.kafka.KafkaConfig;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class DemoKafkaConsumer {
  @KafkaListener(
      topics = "${demo-kafka.test.topic-name}",
      containerFactory = KafkaConfig.BATCH_LISTENER
  )
  public void demoListener(
      List<ConsumerRecord<String, String>> messages,
      Acknowledgment acknowledgment
  ) {
    System.out.println("Received batch: " + messages);
    System.out.println(acknowledgment);
  }
}
