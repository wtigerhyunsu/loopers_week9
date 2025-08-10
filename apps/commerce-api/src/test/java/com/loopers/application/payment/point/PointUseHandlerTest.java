package com.loopers.application.payment.point;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.application.payment.handler.PointUseHandler;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
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
class PointUseHandlerTest {

  @Autowired
  PointUseHandler pointUseHandler;

  @Autowired
  PointRepository pointRepository;

  @Autowired
  PointJpaRepository pointJpaRepository;
  
  @Autowired
  private DatabaseCleanUp databaseCleanUp;
  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }
  @Test
  @DisplayName("포인트가 사용이 되어질때, 포인트가 감소한다.")
  void returnDecreasedPoint_whenPointUsed() {
    //given
    String userId = "userId";
    PointModel afterPoint = pointRepository.save(new PointModel(userId, BigInteger.valueOf(5000)));
    BigInteger usePoint = BigInteger.valueOf(500);
    //when
    pointUseHandler.use(userId, usePoint);
    //then
    PointModel currentPoint = pointJpaRepository.findByUserId(userId).get();
    assertThat(currentPoint.getPoint()).isEqualTo(afterPoint.getPoint().subtract(usePoint));
  }

  @DisplayName("동일한 유저가 서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다.")
  @Test
  void concurrencyTest_pointShouldBeProperlyDontDuplicatedWhenOrdersCreated() throws InterruptedException {
    // Given
    String userId = "userId";
    pointRepository.save(new PointModel(userId, BigInteger.valueOf(10_000)));
    int threadCount = 1000;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          pointUseHandler.use(userId, BigInteger.ONE);
        } catch (Exception e) {
          System.out.println("실패: " + e.getMessage());
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    PointModel pointModel = pointJpaRepository.findByUserId(userId).get();
    assertThat(pointModel.getPoint()).isEqualTo(9000);
  }
}
