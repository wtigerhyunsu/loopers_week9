package com.loopers.infrastructure.catalog.product;

import com.loopers.application.catalog.product.ProductPublisher;
import com.loopers.data_platform.UserTrackingData;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCorePublisher implements ProductPublisher {
  private final ApplicationEventPublisher publisher;

  public void send(String userId, String message) {
    publisher.publishEvent(new UserTrackingData(
        userId,
        "PRODUCT_VIEW",
        message,
        true,
        ZonedDateTime.now()
    ));
  }

  public void fail(String userId, String failMessage) {
    publisher.publishEvent(new UserTrackingData(
        userId,
        "PRODUCT_VIEW",
        false,
        failMessage,
        ZonedDateTime.now()
    ));
  }

}
