package com.loopers.application.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.domain.catalog.product.stock.StockRepository;
import com.loopers.infrastructure.catalog.product.ProductJpaRepository;
import com.loopers.infrastructure.catalog.product.stock.StockJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/test-fixture.sql")
public class OrderStockProcessorTest {
  @Autowired
  private ProductJpaRepository productJpaRepository;

  @Autowired
  private StockJpaRepository stockJpaRepository;

  @Autowired
  private StockProcessor stockProcessor;
  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }


  @DisplayName("동시에 주문해도 재고가 정상적으로 차감된다.")
  @Test
  public void concurrencyTest_stockShouldBeProperlyDecreasedWhenOrdersCreated() throws InterruptedException {
    int threadCount = 1000;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          stockProcessor.decreaseStock(
              7L, 1L);
        } catch (Exception e) {
          System.out.println("실패: " + e.getMessage());
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    StockModel stockModel = stockJpaRepository.findByProductId(7L).get();
    assertThat(stockModel.getStock().getStock()).isEqualTo(0);
  }
}
