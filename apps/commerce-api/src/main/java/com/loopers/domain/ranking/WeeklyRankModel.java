package com.loopers.domain.ranking;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mv_product_rank_weekly",
    indexes = {
        @Index(name = "ix_mv_weekly_rank", columnList = "period_start,period_end,rank"),
        @Index(name = "ix_mv_weekly_product", columnList = "product_id,period_start")
    })
public class WeeklyRankModel {

  @EmbeddedId
  private WeeklyRankId id;

  @Column(name = "period_end", nullable = false)
  private LocalDate periodEnd;

  @Column(name = "score", nullable = false)
  private Long score;

  @Column(name = "rank", nullable = false)
  private Integer rank;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  protected WeeklyRankModel() {}

  public WeeklyRankModel(WeeklyRankId id, LocalDate periodEnd, Long score, Integer rank, LocalDateTime updatedAt) {
    this.id = id;
    this.periodEnd = periodEnd;
    this.score = score;
    this.rank = rank;
    this.updatedAt = updatedAt;
  }

  public WeeklyRankId getId() { return id; }
  public LocalDate getPeriodEnd() { return periodEnd; }
  public Long getScore() { return score; }
  public Integer getRank() { return rank; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

  public void setId(WeeklyRankId id) { this.id = id; }
  public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
  public void setScore(Long score) { this.score = score; }
  public void setRank(Integer rank) { this.rank = rank; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

