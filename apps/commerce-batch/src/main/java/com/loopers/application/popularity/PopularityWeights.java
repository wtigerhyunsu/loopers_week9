package com.loopers.application.popularity;

public class PopularityWeights {
  private LikeWeight like;
  private PurchaseWeight purchase;
  private ViewWeight view;

  public LikeWeight getLike() { return like; }
  public void setLike(LikeWeight like) { this.like = like; }
  public PurchaseWeight getPurchase() { return purchase; }
  public void setPurchase(PurchaseWeight purchase) { this.purchase = purchase; }
  public ViewWeight getView() { return view; }
  public void setView(ViewWeight view) { this.view = view; }
}

