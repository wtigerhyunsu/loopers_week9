package com.loopers.application.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointFacade {
  private final UserRepository userRepository;
  private final PointRepository pointRepository;

  public PointInfo get(String userId) {
    if (userRepository.get(userId).isEmpty()) {
      return null;
    }
    PointModel pointModel = pointRepository.get(userId).orElseThrow(
        () -> new CoreException(ErrorType.BAD_REQUEST, "해당계정은 포인트를 충전하지 않았습니다.")
    );

    return PointInfo.from(pointModel);
  }

  @Transactional
  public PointChargeInfo charge(String userId, BigInteger point) {

    Optional<UserModel> userExists = userRepository.get(userId);

    if (userExists.isEmpty()) {
      throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 계정으로 충전할 수 없습니다.");
    }

    PointModel model = pointRepository.get(userId).orElse(null);

    if (model == null) {
      pointRepository.save(new PointModel(userId, point));
      return new PointChargeInfo(userId, point);
    }

    // 충전
    model.charge(point);

    return new PointChargeInfo(userId, model.getPoint());
  }

}
