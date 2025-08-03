package com.loopers.interfaces.api.product;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class ProductV1Dto {

  class Search {
    record Response(List<Contents> contents,
                    int page,
                    int size,
                    int totalElements,
                    int totalPages) {

    }

    record Contents(Long id, String name) {

    }


  }

  class Get {
    record Response(Long id, String name, BigInteger price, String description,
    LocalDateTime createdAt, LocalDateTime updatedAt) {}
  }

}
