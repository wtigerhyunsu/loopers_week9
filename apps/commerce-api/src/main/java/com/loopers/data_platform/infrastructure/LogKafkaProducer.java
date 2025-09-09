package com.loopers.data_platform.infrastructure;

import com.loopers.data_platform.application.LogProducer;
import com.loopers.data_platform.model.LogMessage;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogKafkaProducer implements LogProducer {
  private final KafkaTemplate<Object, Object> kafkaTemplate;
  private final MessageConverter converter;

  private final static String TOPIC = "LOGGING_V1";

  public void send(String userid, String message) {
    kafkaTemplate.send(TOPIC, userid, converter.convert(
        new Message(converter.convert(new LogMessage(userid, message)))
    ));
  }
}
