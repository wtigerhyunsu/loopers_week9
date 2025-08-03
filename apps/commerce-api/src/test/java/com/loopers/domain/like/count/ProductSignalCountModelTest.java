//package com.loopers.domain.like.count;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//
//import com.loopers.support.error.CoreException;
//import com.loopers.support.error.ErrorType;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//class ProductSignalCountModelTest {
//
//  @DisplayName("좋아요는 상품 ID가 존재하지 않는다면, `404 NotFound`가 발생합니다.")
//  @Test
//  void throwNotFound_whenNotExitsProductId() {
//    //given
//    Long productId = null;
//    // when
//    CoreException result = assertThrows(CoreException.class, () -> ProductSignalCountModel.of(productId, 10));
//    // then
//    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
//  }
//
//  @DisplayName("좋아요 increase()가 호출이 된 경우, count가 1증가한다.")
//  @ParameterizedTest
//  @ValueSource(ints = {1, 2, 10, 100, 1000})
//  void returnPlusOne_whenCallbackIncrease(int count) {
//    ProductSignalCountModel productSignalCountModel = ProductSignalCountModel.of(1L, count);
//    productSignalCountModel.increase();
//    assertThat(productSignalCountModel.getLikeCount()).isEqualTo(count + 1);
//  }
//
//  // 좋아요 해제를 누르는 경우
//  @DisplayName("좋아요 count가 0이하인 경우에서, `400 Bad Request`가 발생합니다.")
//  @ParameterizedTest
//  @ValueSource(ints = {-1, 0})
//  void throwBadRequest_whenLikedCountUnderZero(int count) {
//    ProductSignalCountModel productSignalCountModel = ProductSignalCountModel.of(1L, count);
//    // when
//    CoreException result = assertThrows(CoreException.class, () -> productSignalCountModel.decrease(true));
//    // then
//    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
//  }
//
//  @DisplayName("좋아요 decrease()가 호출이 된 경우, count가 1감소한다.")
//  @Test
//  void returnPlusOne_whenCallbackDecrease() {
//    int count = 10;
//    ProductSignalCountModel productSignalCountModel = ProductSignalCountModel.of(1L, count);
//    productSignalCountModel.decrease(true);
//    assertThat(productSignalCountModel.getLikeCount()).isEqualTo(count - 1);
//  }
//}
