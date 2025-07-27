package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User API 입니다.")
public interface UserV1ApiSpec {

  @Operation(
      summary = "회원가입",
      description = "회원가입을 합니다."
  )
  ApiResponse<UserV1Dto.Resister.UserResponse> createUser(
      @Schema(name = "회원가입 정보", description = "회원가입을 하게 되는 계정의 정보")
      UserV1Dto.Resister.UserRequest request
  );

  @Operation(
      summary = "계정조회",
      description = "계정조회를 합니다."
  )
  ApiResponse<UserV1Dto.UserGetResponse> getUser(
      @Schema(name = "계정 조회", description = "조회하게 되는 계정의 정보")
      String userId
  );

}
