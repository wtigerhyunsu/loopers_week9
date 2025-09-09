package com.loopers.domain.event;

public interface EventHandledRepository {
  boolean check(String eventId);

  void save(String eventId);
}
