package com.loopers.interfaces.api.like;


import java.util.List;

public class LikeV1Dto {

  class Register {
    record Response(String userId, Long productId, boolean status) {}
  }
  class Unregister {
    record Response(String userId, Long productId, boolean status) {}
  }


  class Get {
    record Response(List<Contents> contents,
                    int page,
                    int size,
                    int totalElements,
                    int totalPages) {}
    record Contents(Long productId, String productName) {}
  }

}
