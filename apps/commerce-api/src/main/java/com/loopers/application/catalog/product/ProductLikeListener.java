package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.status.ProductStatusRepository;
import com.loopers.domain.like.LikeDecreaseEvent;
import com.loopers.domain.like.LikeIncreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLikeListener {
  private final ProductStatusRepository repository;

  @Async
  @EventListener
  public void increase(LikeIncreaseEvent event) {
    repository.increase(event.productId());
  }


  @Async
  @EventListener
  public void decrease(LikeDecreaseEvent event) {
    repository.decrease(event.productId());
  }

}
