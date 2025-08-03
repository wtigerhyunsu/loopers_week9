package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.Search.Contents;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller {


  @GetMapping
  public ApiResponse<ProductV1Dto.Search.Response> search() {
    return ApiResponse.success(
        new ProductV1Dto.Search.Response(
            List.of(new Contents(1L,"상품1"),new Contents(2L,"상품2")),
            1,1,1,1
        )
    );
  }

  @GetMapping("/{productId}")
  public ApiResponse<ProductV1Dto.Get.Response> search(@PathVariable Long productId) {
    return ApiResponse.success(
        new ProductV1Dto.Get.Response(
            productId,"상품1", BigInteger.valueOf(2000),"이건 검정색이고 암튼 좋아요",
            LocalDateTime.now(),LocalDateTime.now()
        )
    );
  }
}
