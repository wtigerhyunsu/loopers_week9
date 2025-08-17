package com.loopers.interfaces.api.product;

import com.loopers.application.catalog.product.ProductFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductV1Controller implements ProductV1ApiSpec {
  private final ProductFacade productFacade;

  @Override
  @GetMapping
  public ApiResponse<ProductV1Dto.Search.Response> search(

      @RequestParam(required = false) Long brandId,
      @RequestParam(required = false) String brandName,
      @RequestParam(required = false) String productName,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) Integer currentPage,
      @RequestParam(required = false) Integer perSize
  ) {
    ProductV1Condition condition = ProductV1Condition.builder()
        .brandId(brandId)
        .brandName(brandName)
        .productName(productName)
        .sort(sort)
        .currentPage(currentPage)
        .perSize(perSize).build();
    return ApiResponse.success(
        ProductV1Dto.Search.Response.from(productFacade.search(condition.toCommand()))
    );
  }

  @Override
  @GetMapping("/{productId}")
  public ApiResponse<ProductV1Dto.Get.Response> get(@PathVariable Long productId) {
    return ApiResponse.success(ProductV1Dto.Get.Response.from(productFacade.get(productId)));
  }

  @PostMapping ("/rank")
  public ApiResponse<?> rank() {
    productFacade.rank();
    return ApiResponse.success();
  }


}
