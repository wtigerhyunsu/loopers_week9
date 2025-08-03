package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LikeModelTest {

  @Test
  @DisplayName("계정 아이디를 입력하지 않는다면, `400 BadRequest`를 반환한다.")
  void throw400BadRequestException_whenInputUserId() {
    //given
    String userId = null;
    Long productId = 1L;
    //when
    CoreException result = assertThrows(CoreException.class, () -> LikeModel.of(userId, productId));
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }


  @Test
  @DisplayName("상품 아이디를 입력하지 않는다면, `400 BadRequest`를 반환한다.")
  void throw400BadRequestException_whenInputProductId() {
    //given
    String userId = "user";
    Long productId = null;
    //when
    CoreException result = assertThrows(CoreException.class, () ->  LikeModel.of(userId, productId));
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }



}
