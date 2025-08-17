package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.like.LikeV1Dto.Register.Response;
import com.loopers.interfaces.api.like.LikeV1Dto.Unregister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like V1 API", description = "Like API 입니다.")
public interface LikeV1ApiSpec {

  @Operation(
      summary = "좋아요 등록",
      description = "상품에 좋아요를 합니다."
  )
  ApiResponse<Response> register(
      @Schema(name = "계정 아이디", description = "좋아요를 누를 계정의 아이디") String userId,
      @Schema(name = "상품 아이디", description = "좋아요를 누릴 상품의 아이디") Long productId);

  @Operation(
      summary = "좋아요 해제",
      description = "좋아요가 지정된 상품의 좋아요를 해제합니다."
  )
  ApiResponse<Unregister.Response> unregister(
      @Schema(name = "계정 아이디", description = "좋아요를 해제할 계정의 아이디") String userId,
      @Schema(name = "상품 아이디", description = "좋아요를 해제될 상품의 아이디") Long productId);


}
