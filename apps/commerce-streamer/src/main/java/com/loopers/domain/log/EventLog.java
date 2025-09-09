package com.loopers.domain.log;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "event_log")
public class EventLog extends BaseEntity {
  private String userId;
  private String log;
  private LocalDate eventTime;

  protected EventLog() {
  }

  public EventLog(String userId, String log, LocalDate eventTime) {
    this.userId = userId;
    this.log = log;
    this.eventTime = eventTime;
  }
}
