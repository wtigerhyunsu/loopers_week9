package com.loopers.interfaces.api.brand;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.brand.BrandV1Dto.Product;
import com.loopers.interfaces.api.brand.BrandV1Dto.Response;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandV1Controller {

  @GetMapping("/{brandId}")
  public ApiResponse<BrandV1Dto.Response> getBrand(@PathVariable Long brandId)
  {

    return ApiResponse.success(new Response(
        brandId,
        "브랜드명",
        List.of(new Product(1L,"프로젝트1")
        ,new Product(2L,"프로젝트2"))
    ));
  }
}
