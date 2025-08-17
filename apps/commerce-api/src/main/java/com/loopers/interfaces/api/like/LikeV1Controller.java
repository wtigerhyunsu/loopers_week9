package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.like.LikeV1Dto.Get;
import com.loopers.interfaces.api.like.LikeV1Dto.Get.Contents;
import com.loopers.interfaces.api.like.LikeV1Dto.Register.Response;
import com.loopers.interfaces.api.like.LikeV1Dto.Unregister;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like/products")
@RequiredArgsConstructor
public class LikeV1Controller implements LikeV1ApiSpec {
  private final LikeFacade likeFacade;

  @Override
  @PostMapping("/{productId}")
  public ApiResponse<Response> register(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long productId) {
    likeFacade.like(userId, productId);
    return ApiResponse.success(Response.from(userId, productId));
  }

  @Override
  @DeleteMapping("/{productId}")
  public ApiResponse<Unregister.Response> unregister(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long productId) {
    likeFacade.unlike(userId, productId);
    return ApiResponse.success(Unregister.Response.from(userId, productId));
  }

  @GetMapping()
  public ApiResponse<LikeV1Dto.Get.Response> get(
      @RequestHeader("X-User-Id") String userId) {
    return ApiResponse.success(new Get.Response(
        List.of(new Contents(1L, "상품1"),
            new Contents(1L, "상품1")),
        1, 1, 1, 1
    ));
  }

}
