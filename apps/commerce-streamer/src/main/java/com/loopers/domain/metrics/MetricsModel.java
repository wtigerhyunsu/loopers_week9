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
  private LocalDate date;

  protected MetricsModel() {
  }

  public MetricsModel(Long productId, Long views, Long likes, Long sales, LocalDate date) {
    this.productId = productId;
    this.views = views;
    this.likes = likes;
    this.sales = sales;
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

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }
}
