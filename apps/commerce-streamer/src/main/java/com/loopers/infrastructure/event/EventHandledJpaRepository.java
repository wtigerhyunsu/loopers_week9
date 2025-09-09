package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandled;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventHandledJpaRepository extends JpaRepository<EventHandled, Long> {
  Optional<EventHandled> findByEventId(String eventId);
}
