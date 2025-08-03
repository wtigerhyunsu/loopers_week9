package com.loopers.domain.order.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Getter;

@Embeddable
@Getter
public class OrderNumber {

  @Column(length = 25, unique = true)
  private String number;

  public OrderNumber() {
    this.number = generate();
  }

  private String generate() {
    String uuidPrefix = UUID.randomUUID().toString().substring(0, 8);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return formatter.format(LocalDateTime.now()) + "-" + uuidPrefix;
  }

}
