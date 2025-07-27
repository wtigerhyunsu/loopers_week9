package com.loopers.application.user;

import com.loopers.domain.user.UserModel;

public record UserInfo(String userId, String email, String birthday, String gender) {

  public static UserInfo from(UserModel model) {
    return new UserInfo(
        model.getUserId(),
        model.getEmail(),
        model.getBirthday().toString(),
        model.getGender()
    );
  }

  public UserModel toModel() {
    return new UserModel(
        userId,
        email,
        birthday,
        gender);
  }
}
