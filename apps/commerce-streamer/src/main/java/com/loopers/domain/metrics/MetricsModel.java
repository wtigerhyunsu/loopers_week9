package com.loopers.domain.metrics;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "product_metrics")
public class MetricsModel extends BaseEntity {
  private Long productId;
  private Long views;
  private Long likes;
  private Long sales;
  // total sales amount (won) per day
  private Long amount;
  private LocalDate date;

  protected MetricsModel() {
  }

  public MetricsModel(Long productId, Long views, Long likes, Long sales, LocalDate date) {
    this.productId = productId;
    this.views = views;
    this.likes = likes;
    this.sales = sales;
    this.amount = 0L;
    this.date = date;
  }

  public MetricsModel(Long productId, Long views, Long likes, Long sales, Long amount, LocalDate date) {
    this.productId = productId;
    this.views = views;
    this.likes = likes;
    this.sales = sales;
    this.amount = amount == null ? 0L : amount;
    this.date = date;
  }

  public void updateViews() {
    this.views++;
  }

  public void updateLikes(long like) {
    this.likes += like;

  }

  public void updateSales(Long sales) {
    this.sales += sales;
  }

  public void updateAmount(Long amount) {
    this.amount += amount;
  }


  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getViews() {
    return views;
  }

  public void setViews(Long views) {
    this.views = views;
  }

  public Long getLikes() {
    return likes;
  }

  public void setLikes(Long likes) {
    this.likes = likes;
  }

  public Long getSales() {
    return sales;
  }

  public void setSales(Long sales) {
    this.sales = sales;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
}
