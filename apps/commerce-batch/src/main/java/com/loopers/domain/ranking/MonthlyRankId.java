package com.loopers.domain.ranking;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class MonthlyRankId implements Serializable {
  @Column(name = "period_start", nullable = false)
  private LocalDate periodStart;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  protected MonthlyRankId() {}

  public MonthlyRankId(LocalDate periodStart, Long productId) {
    this.periodStart = periodStart;
    this.productId = productId;
  }

  public LocalDate getPeriodStart() { return periodStart; }
  public Long getProductId() { return productId; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MonthlyRankId that = (MonthlyRankId) o;
    return Objects.equals(periodStart, that.periodStart) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() { return Objects.hash(periodStart, productId); }
}

