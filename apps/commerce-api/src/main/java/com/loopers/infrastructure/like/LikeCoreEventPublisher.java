package com.loopers.infrastructure.like;

import com.loopers.application.like.LikeEventPublisher;
import com.loopers.data_platform.application.UserTrackingData;
import com.loopers.domain.like.LikeDecreaseEvent;
import com.loopers.domain.like.LikeIncreaseEvent;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeCoreEventPublisher implements LikeEventPublisher {
  private final ApplicationEventPublisher publisher;

  public void increase(String userId, Long productId) {
    log.info("userId : {}가 productId :{}에 좋아요를 증가시켰습니다.", userId, productId);
    publisher.publishEvent(new LikeIncreaseEvent(userId, productId));
  }

  public void decrease(String userId, Long productId, int current) {
    log.info("userId : {}가 productId :{}에 좋아요를 감소시켰습니다.", userId, productId);
    publisher.publishEvent(new LikeDecreaseEvent(userId, productId, current));
  }


  public void send(String userId, String type, String message) {
    publisher.publishEvent(new UserTrackingData(
        userId,
        "LIKE_COUNT_" + type.toUpperCase(),
        message,
        true,
        ZonedDateTime.now()
    ));
  }

  public void fail(String userId, String type, String failReason) {
    publisher.publishEvent(new UserTrackingData(
        userId,
        "LIKE_COUNT_" + type.toUpperCase(),
        false,
        failReason,
        ZonedDateTime.now()
    ));
  }


}
