package com.loopers.infrastructure.log;

import com.loopers.domain.log.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogJpaRepository extends JpaRepository<EventLog, Long> {
}
