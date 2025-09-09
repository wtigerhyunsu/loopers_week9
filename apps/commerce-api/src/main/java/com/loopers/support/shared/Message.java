package com.loopers.support.shared;

import java.time.LocalDate;
import java.util.UUID;

public record Message(
    String eventId,
    String payload,
    Long eventTime
) {

  public Message(String payload) {
    this(UUID.randomUUID().toString(), payload, LocalDate.now().toEpochDay());
  }
}

