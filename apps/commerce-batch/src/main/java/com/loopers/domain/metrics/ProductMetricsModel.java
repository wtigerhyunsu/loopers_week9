package com.loopers.domain.metrics;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "product_metrics")
public class ProductMetricsModel {
  @Id
  private Long id;
  private Long productId;
  private Long views;
  private Long likes;
  private Long sales;
  private Long amount;
  private LocalDate date;

  protected ProductMetricsModel() {}

  public Long getId() { return id; }
  public Long getProductId() { return productId; }
  public Long getViews() { return views; }
  public Long getLikes() { return likes; }
  public Long getSales() { return sales; }
  public Long getAmount() { return amount; }
  public LocalDate getDate() { return date; }
}

