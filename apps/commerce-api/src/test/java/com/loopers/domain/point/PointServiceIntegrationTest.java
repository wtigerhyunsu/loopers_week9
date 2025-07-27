package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class PointServiceIntegrationTest {
  @MockitoSpyBean
  private PointService pointService;
  @MockitoSpyBean
  private UserJpaRepository userJpaRepository;
  @MockitoSpyBean
  private PointJpaRepository pointJpaRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("포인트를 조회 했을때, ")
  @Nested
  class Get {
    /**
     * - [ ]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다. - [ ]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     */

    @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
    @Test
    void returnSavingPointWhenExitsUserId() {
      //arrange
      String userId = "test";
      int point = 5000;
      userJpaRepository.save(new UserModel(userId, "test@test.com", "2020-01-01", "M"));
      pointJpaRepository.save(new PointModel(userId, point));
      // act
      PointModel pointModel = pointService.get(userId);
      // assert
      assertThat(pointModel.getPoint()).isEqualTo(point);
    }

    @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
    @Test
    void returnNullWhenNotExitsUserId() {
      //arrange
      String userId = "test";
      int point = 5000;
      pointJpaRepository.save(new PointModel(userId, point));
      // act
      PointModel pointModel = pointService.get(userId);
      // assert
      assertThat(pointModel).isNull();
    }

  }


  @DisplayName("포인트를 충전할때,")
  @Nested
  class Charge {

    @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
    @Test
    void returnNotFoundException_whenChargingWithNonExistentUserId() {
      //arrange
      String userId = "notExist";
      int point = 5000;
      // act
      CoreException result = assertThrows(CoreException.class, () -> pointService.charge(userId, point));
      // assert
      assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }
  }
}
