package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.Search.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "Product API 입니다.")
public interface ProductV1ApiSpec {

  @Operation(
      summary = "검색",
      description = "상품을 검색 합니다."
  )
  ApiResponse<Response> search(
      @Schema(name = "브랜드 아이디", description = "특정 브랜드 아이디에 해당하는 상품 리스트 조회") Long brandId,
      @Schema(name = "브랜드 명", description = "특정 브랜드 명에 해당하는 상품 리스트 조회") String brandName,
      @Schema(name = "상품 명", description = "특정 상품 명에 해당하는 상품 리스트 조회") String productName,
      @Schema(name = "정렬 조건", description = "검색에 활용되어지는 정렬 조건") String sort,
      @Schema(name = "현재 페이지", description = "검색할 페이지") Integer currentPage,
      @Schema(name = "필요 갯수", description = "검색되어지는 상품 갯수") Integer perSize);

  @Operation(
      summary = "조회",
      description = "상품을 단일 조회합니다."
  )
  ApiResponse<ProductV1Dto.Get.Response> get(
      @Schema(name = "로그인 계정") String userId,
      @Schema(name = "상품 ID", description = "상세 조회할 상품의 ID")
      Long productId);
}
