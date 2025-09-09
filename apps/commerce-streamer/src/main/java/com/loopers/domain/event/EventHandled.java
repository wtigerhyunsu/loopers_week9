package com.loopers.domain.event;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_handled")
public class EventHandled extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String eventId;

  protected EventHandled() {
  }

  public EventHandled(String eventId) {
    this.eventId = eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public String getEventId() {
    return eventId;
  }
}
