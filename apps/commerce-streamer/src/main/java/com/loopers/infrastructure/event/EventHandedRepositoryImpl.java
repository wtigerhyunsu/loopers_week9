package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandled;
import com.loopers.domain.event.EventHandledRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EventHandedRepositoryImpl implements EventHandledRepository {
  private final EventHandledJpaRepository repository;

  public EventHandedRepositoryImpl(EventHandledJpaRepository repository) {
    this.repository = repository;
  }


  @Override
  public boolean check(String eventId) {
    Optional<EventHandled> handled = repository.findByEventId(eventId);
    return handled.isEmpty();
  }

  @Override
  public void save(String eventId) {
    repository.save(new EventHandled(eventId));
  }
}
