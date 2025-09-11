package com.loopers.application.popularity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "popularity")
public class PopularityProperties {
  private Weights weights = new Weights();
  private int keyTtlHours = 24;

  public Weights getWeights() {
    return weights;
  }

  public void setWeights(Weights weights) {
    this.weights = weights;
  }

  public int getKeyTtlHours() {
    return keyTtlHours;
  }

  public void setKeyTtlHours(int keyTtlHours) {
    this.keyTtlHours = keyTtlHours;
  }

  public static class Weights {
    private long like = 1;
    private Purchase purchase = new Purchase();

    public long getLike() {
      return like;
    }

    public void setLike(long like) {
      this.like = like;
    }

    public Purchase getPurchase() {
      return purchase;
    }

    public void setPurchase(Purchase purchase) {
      this.purchase = purchase;
    }

    public static class Purchase {
      private long qty = 1;
      private long amountPerKrw1000 = 1;

      public long getQty() { return qty; }
      public void setQty(long qty) { this.qty = qty; }
      public long getAmountPerKrw1000() { return amountPerKrw1000; }
      public void setAmountPerKrw1000(long amountPerKrw1000) { this.amountPerKrw1000 = amountPerKrw1000; }
    }
  }
}
