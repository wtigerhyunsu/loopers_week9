package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "Point API 입니다.")
public interface PointV1ApiSpec {

  @Operation(
      summary = "포인트 조회",
      description = "포인트 조회를 합니다."
  )
  ApiResponse<PointV1Dto.PointResponse> get(
      @Schema(name = "계정 ID", description = "로그인 ID")
      String userId
  );

  @Operation(
      summary = "포인트 충전",
      description = "포인트 충전을 합니다."
  )
  ApiResponse<PointV1Dto.ChargeResponse> charge(
      @Schema(name = "계정 ID", description = "로그인 ID")
      String userId,
      @Schema(name = "충전할 포인트", description = "충전할 포인트 요청정보")
      PointV1Dto.ChargeRequest request
  );

}
