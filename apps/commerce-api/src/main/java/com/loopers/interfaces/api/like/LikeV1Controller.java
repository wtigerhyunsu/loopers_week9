package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.like.LikeV1Dto.Get;
import com.loopers.interfaces.api.like.LikeV1Dto.Get.Contents;
import com.loopers.interfaces.api.like.LikeV1Dto.Register.Response;
import com.loopers.interfaces.api.like.LikeV1Dto.Unregister;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like/products")
public class LikeV1Controller {

  @PostMapping("/{productId}")
  public ApiResponse<LikeV1Dto.Register.Response> register(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long productId) {
    return ApiResponse.success(new Response(
        userId,
        productId,
        true
    ));
  }

  @DeleteMapping("/{productId}")
  public ApiResponse<LikeV1Dto.Unregister.Response> unregister(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long productId) {
    return ApiResponse.success(new Unregister.Response(
        userId,
        productId,
        false));
  }

  @GetMapping()
  public ApiResponse<LikeV1Dto.Get.Response> get(
      @RequestHeader("X-User-Id") String userId) {
    return ApiResponse.success(new Get.Response(
        List.of(new Contents(1L,"상품1"),
            new Contents(1L,"상품1")),
        1,1,1,1
    ));
  }

}
