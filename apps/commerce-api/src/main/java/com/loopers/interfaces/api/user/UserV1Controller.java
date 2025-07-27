package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.UserGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {
  private final UserFacade userFacade;

  @Override
  @PostMapping
  public ApiResponse<UserV1Dto.Resister.UserResponse> createUser(@RequestBody @Validated UserV1Dto.Resister.UserRequest request) {
    UserInfo user = userFacade.createUser(request.toUserInfo());
    return ApiResponse.success(UserV1Dto.Resister.UserResponse.from(user));
  }

  @Override
  @GetMapping("/me")
  public ApiResponse<UserV1Dto.UserGetResponse> getUser(@RequestHeader(name = "X-User-Id") String userId) {
    UserInfo user = userFacade.getUser(userId);
    return ApiResponse.success(UserGetResponse.from(user));
  }
}
