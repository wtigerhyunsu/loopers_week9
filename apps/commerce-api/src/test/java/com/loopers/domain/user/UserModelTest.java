package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @ValueSource(strings = {"userI123456", "_-+=!", " ", "12345678901", "useruseruser"})
    void throwBadRequestException_whenUserIdIsAlphaNumericAndLengthAtLeast10(String userId) {
      // arrange

      // act
      CoreException result = assertThrows(CoreException.class, () -> new UserId(userId));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("이메일 형식`aaa@yyy.com`이 틀리면, BAD_REQUEST 예외가 발생합니다.")
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

    @DisplayName("생년월일형식`yyyy-MM-dd` 이 틀리면, BAD_REQUEST 예외가 발생합니다.")
    @Test
    void throwsBadRequestException_whenBirthIsWrongPattern() {
      // arrange
      String birth = "wrong";
      // act
      CoreException result = assertThrows(CoreException.class, () -> new BirthDay(birth));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

    @DisplayName("모든 데이터(아이디,이메일,생년월일)이 존재한다면, 계정 데이터를 리턴합니다.")
    @Test
    void returnUserInfo_whenAllDataExits() {
      //given
      String userId = "userId";
      String email = "email@test.com";
      String birth = "2020-11-30";

      // when
      UserModel userModel = UserModel.create()
          .userId(userId)
          .email(email)
          .birthday(birth)
          .build();

      // then
      assertAll(() -> assertThat(userModel.getUserId()).isEqualTo(userId),
          () -> assertThat(userModel.getEmail()).isEqualTo(email),
          () -> assertThat(userModel.getBirthday()).isEqualTo(birth));
    }
  }
}
