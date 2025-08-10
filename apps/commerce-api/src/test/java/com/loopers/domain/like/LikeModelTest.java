package com.loopers.domain.like;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LikeModelTest {

  @Test
  @DisplayName("계정 아이디를 입력하지 않는다면, `IllegalArgumentException`를 반환한다.")
  void throw400BadRequestException_whenInputUserId() {
    //given
    String userId = null;
    Long productId = 1L;
    //when&then
    Exception result = assertThrows(IllegalArgumentException.class, () -> LikeModel.of(userId, productId));

  }


  @Test
  @DisplayName("상품 아이디를 입력하지 않는다면, `IllegalArgumentException`을 반환한다.")
  void throw400BadRequestException_whenInputProductId() {
    //given
    String userId = "user";
    Long productId = null;
    //when&then
    Exception result = assertThrows(IllegalArgumentException.class, () -> LikeModel.of(userId, productId));
  }



}
