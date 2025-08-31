package com.loopers.application.payment;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentUsePointHandler {
  private final PointRepository pointRepository;

  @Transactional
  public void use(String userId, BigInteger amount) {
    PointModel hasPoint = pointRepository.getWithLock(userId).orElseThrow(
        () -> new CoreException(ErrorType.BAD_REQUEST, "사용할 수 있는 포인트가 존재하지 않습니다.")
    );
    hasPoint.use(amount);
  }
}
