package com.loopers.interfaces.api.product;

import com.loopers.application.catalog.product.ProductContents;
import com.loopers.application.catalog.product.ProductGetInfo;
import com.loopers.application.catalog.product.ProductSearchInfo;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

public class ProductV1Dto {

  static class Search {
    @Builder
    record Response(List<Contents> contents,
                    int page,
                    int size,
                    long totalElements,
                    int totalPages) {

      public static Response from(ProductSearchInfo search) {

        return Response.builder()
            .contents(
                search.contents().stream().map(Contents::new).toList()
            )
            .page(search.page())
            .size(search.size())
            .totalElements(search.totalElements())
            .totalPages(search.totalPages())
            .build();
      }
    }

    record Contents(Long id, String name, int likeCount) {

      public Contents(ProductContents contents) {
        this(contents.id(), contents.name(), contents.likeCount());
      }
    }


  }


  static class Get {
    record Response(Long productId,
                    String brandName,
                    String productName,
                    BigInteger price,
                    int likedCount,
                    String description,
                    ZonedDateTime createdAt,
                    ZonedDateTime updatedAt) {

      public static Response from(ProductGetInfo productGetInfo) {
        return new Response(productGetInfo.productId(), productGetInfo.brandName(), productGetInfo.productName(),
            productGetInfo.price(),
            productGetInfo.likedCount(), productGetInfo.description(), productGetInfo.createdAt(), productGetInfo.updatedAt());
      }
    }
  }

}
