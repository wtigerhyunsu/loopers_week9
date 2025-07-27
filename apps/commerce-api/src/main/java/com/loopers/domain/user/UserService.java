package com.loopers.domain.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;


  @Transactional
  public UserModel createUser(UserModel userModel) {
    userRepository.duplicateUserId(userModel.getUserId());
    return userRepository.save(userModel);
  }

  public UserModel getUser(String userId) {
    Optional<UserModel> userOptional = userRepository.findByUserId(userId);

    return userOptional.orElse(null);

  }
}
