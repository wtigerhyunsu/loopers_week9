package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class UserServiceIntegrationTest {
  @MockitoSpyBean
  private UserService userService;
  @MockitoSpyBean
  private UserJpaRepository userJpaRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;


  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("회원가입을 했을때, ")
  @Nested
  class JoinTest {

    @DisplayName("User 저장이 수행된다.")
    @Test
    void returnsExampleInfo_saveUser() {
      // arrange
      UserModel userModel = new UserModel("userId", "test@test.com", "2020-01-01","F");
      // act
      userService.createUser(userModel);
      // assert
      Mockito.verify(userJpaRepository, Mockito.times(1)).save(userModel);
    }


    @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
    @Test
    void throwsBadRequestException_saveUser_whenAlreadyId() {
      // arrange
      UserModel userModel = new UserModel("userId", "test@test.com", "2020-01-01","F");
      userService.createUser(userModel);
      // act
      CoreException result = assertThrows(CoreException.class, () -> userService.createUser(userModel));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }

  }

  @DisplayName("계정조회를 했을때, ")
  @Nested
  class Get {

    /**
     * - [ ]  해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.
     * - [ ]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     */

    @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
    @Test
    void returns_userInfo_whenRetire() {
      // arrange
      String userId = "my";
      userService.createUser(new UserModel("my", "test@test.com", "2020-01-01","F"));
      // act
      UserModel userModel = userService.getUser(userId);
      // assert
      assertAll(
          () -> assertThat(userModel.getUserId()).isNotNull(),
          () -> assertThat(userModel.getEmail()).isNotNull(),
          () -> assertThat(userModel.getBirthday()).isNotNull(),
          () -> assertThat(userModel.getGender()).isNotNull()
      );
    }

    @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
    @Test
    void returns_null_when_emptyUser() {
      // arrange
      String userId = "my";
     // act
      UserModel userModel = userService.getUser(userId);
      // assert
      assertAll(
          () -> assertThat(userModel).isNull()
      );
    }



  }
}
