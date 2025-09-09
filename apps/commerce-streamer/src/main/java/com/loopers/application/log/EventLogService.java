package com.loopers.application.log;

import com.loopers.domain.event.EventHandledRepository;
import com.loopers.domain.log.EventLog;
import com.loopers.domain.log.EventLogRepository;
import com.loopers.infrastructure.log.LogMessage;
import com.loopers.support.shared.Message;
import com.loopers.support.shared.MessageConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventLogService {
  private final MessageConvert convert;
  private final EventLogRepository repository;
  private final EventHandledRepository eventHandledRepository;

  public EventLogService(MessageConvert convert, EventLogRepository repository, EventHandledRepository eventHandledRepository) {
    this.convert = convert;
    this.repository = repository;
    this.eventHandledRepository = eventHandledRepository;
  }

  public void insert(String message) {

    try {
      Message messageConvert = convert.convert(message, Message.class);
      LogMessage result = convert.convert(messageConvert.getPayload(), LogMessage.class);
      EventLog eventLog = new EventLog(result.userId(), result.message(), messageConvert.getEventTime());
      repository.insert(eventLog);
      eventHandledRepository.save(messageConvert.getEventId());
    } catch (Exception e) {
      throw e;
    }
  }
}
