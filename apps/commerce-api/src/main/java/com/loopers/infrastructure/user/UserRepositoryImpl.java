package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.embeded.UserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository repository;

  @Override
  public UserModel save(UserModel userModel) {
    return repository.save(userModel);
  }

  @Override
  public void duplicateUserId(String userId) {
    Optional<UserModel> exitsUser = repository.findByUserId(new UserId(userId));
    if (exitsUser.isPresent()) {
      throw new CoreException(ErrorType.BAD_REQUEST, "해당 아이디는 이미 가입이 되어있습니다.");
    }
  }

  @Override
  public Optional<UserModel> findByUserId(String userId) {
    return repository.findByUserId(new UserId(userId));

  }
}
