package com.loopers.application.payment;

import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.domain.catalog.product.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockProcessor {
  private final StockRepository stockRepository;


  @Transactional
  public void decreaseStock(Long productId, Long quantity) {
    StockModel stockModel = stockRepository.get(productId);
    stockModel.decrease(quantity);
  }
}
