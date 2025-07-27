package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {

  UserModel save(UserModel userModel);

  void duplicateUserId(String userId);
  Optional<UserModel> findByUserId(String userId);
}
