package com.loopers.application.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserFacade {
  private final UserRepository userRepository;

  @Transactional
  public UserInfo createUser(UserCommand command) {
    userRepository.duplicateUserId(command.userId());

    UserModel userModel = userRepository.save(UserModel.create()
        .userId(command.userId())
        .email(command.email())
        .birthday(command.birthday())
        .gender(command.gender())
        .build());

    return UserInfo.from(userModel);
  }

  public UserInfo getUser(String userId) {
    Optional<UserModel> userOptional = userRepository.get(userId);
    return userOptional.map(UserInfo::from).orElseThrow(
        () -> new CoreException(ErrorType.NOT_FOUND,"해당하는 계정이 존재하지 않습니다.")
    );

  }

}
