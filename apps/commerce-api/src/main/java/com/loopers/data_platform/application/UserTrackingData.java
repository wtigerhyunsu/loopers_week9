package com.loopers.data_platform.application;

import java.time.ZonedDateTime;

public record UserTrackingData(
    String userId,                   // 누가
    Action action,                   // 어떤 행동
    String message,                   // 어떤 API/메서드에서 발생했는지
    boolean status,                   // 성공/실패 (SUCCESS, FAIL)
    String failReason,               // 실패 이유 (status=FAIL일 때만 사용)
    ZonedDateTime timestamp          // 언제 발생했는지
) {
  public UserTrackingData(String userId, String action, String message, boolean status,
                          ZonedDateTime timestamp) {
    this(userId, Action.valueOf(action), message, status, null, timestamp);
  }

  public UserTrackingData(String userId, String action, boolean status, String failReason,
                          ZonedDateTime timestamp) {
    this(userId, Action.valueOf(action), null, status, failReason, timestamp);
  }

}

enum Action {
  LIKE_COUNT_INCREASE,
  LIKE_COUNT_DECREASE,
  ORDER_CREATE,
  PAYMENT_CREATE,
  PRODUCT_VIEW
}
