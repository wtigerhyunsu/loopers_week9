package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.user.embeded.BirthDay;
import com.loopers.domain.user.embeded.Email;
import com.loopers.domain.user.embeded.UserId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserModelTest {


  @DisplayName("회원가입을 했을때, ")
  @Nested
  class JoinTest {

    @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"userI123456","_-+=!"," ","12345678901","useruseruser"})
    void throwBadRequestException_whenUserIdIsAlphaNumericAndLengthAtLeast10(String userId) {
      // arrange

      // act
      CoreException result = assertThrows(CoreException.class, () -> new UserId(userId));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("이메일형식이 틀리면, BAD_REQUEST 예외가 발생합니다.")
    @ParameterizedTest
    @ValueSource(strings = {"email", // ID만 존재하는 경우
                            "email@" // @까지 존재하는 경우"
                 })
    void throwsBadRequestException_whenEmailIsWrongPattern(String email) {
      // arrange
      // act
      CoreException result = assertThrows(CoreException.class, () -> new Email(email));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("생년월일형식이 틀리면, BAD_REQUEST 예외가 발생합니다.")
    @Test
    void throwsBadRequestException_whenBirthIsWrongPattern() {
      // arrange
      String birth = "wrong";
      // act
      CoreException result = assertThrows(CoreException.class, () -> new BirthDay(birth));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
  }
}
