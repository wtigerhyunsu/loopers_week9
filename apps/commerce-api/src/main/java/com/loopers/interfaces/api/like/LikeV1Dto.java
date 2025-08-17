package com.loopers.interfaces.api.like;


import java.util.List;

public class LikeV1Dto {

  static class Register {
    record Response(String userId, Long productId, boolean status) {

      public static Response from(String userId, Long productId) {
        return new Response(userId, productId, true);
      }

    }
  }

  class Unregister {
    record Response(String userId, Long productId, boolean status) {
      public static Response from(String userId, Long productId) {
        return new Unregister.Response(userId, productId, false);
      }
    }
  }


  class Get {
    record Response(List<Contents> contents,
                    int page,
                    int size,
                    int totalElements,
                    int totalPages) {
    }

    record Contents(Long productId, String productName) {
    }
  }

}
