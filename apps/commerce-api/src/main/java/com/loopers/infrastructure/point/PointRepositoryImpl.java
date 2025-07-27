package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.embeded.UserId;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {
  private final PointJpaRepository pointRepository;
  private final UserJpaRepository userRepository;


  @Override
  public PointModel get(String userId) {

    if (userRepository.findByUserId(new UserId(userId)).isEmpty()) {
      return null;
    }

    return pointRepository.findByUserId(userId).orElse(new PointModel(userId));
  }

  @Override
  @Transactional
  public PointModel charge(String userId, int point) {

    Optional<UserModel> userExists = userRepository.findByUserId(new UserId(userId));

    if (userExists.isEmpty()) {
      throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 계정으로 충전할 수 없습니다.");
    }

    PointModel model = pointRepository.findByUserId(userId).orElse(new PointModel(userId));

    model.charge(point);

    pointRepository.save(model);

    return model;
  }
}
