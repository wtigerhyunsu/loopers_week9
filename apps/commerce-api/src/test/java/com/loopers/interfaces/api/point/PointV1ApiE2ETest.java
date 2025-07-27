package com.loopers.interfaces.api.point;

import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.FAIL;
import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

  private final TestRestTemplate testRestTemplate;
  private final UserJpaRepository userJpaRepository;
  private final PointJpaRepository pointJpaRepository;
  private final DatabaseCleanUp databaseCleanUp;

  @Autowired
  public PointV1ApiE2ETest(
      TestRestTemplate testRestTemplate,
      UserJpaRepository userJpaRepository,
      PointJpaRepository pointJpaRepository,

      DatabaseCleanUp databaseCleanUp
  ) {
    this.testRestTemplate = testRestTemplate;
    this.userJpaRepository = userJpaRepository;
    this.pointJpaRepository = pointJpaRepository;
    this.databaseCleanUp = databaseCleanUp;
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }


  @DisplayName("GET /api/v1/points")
  @Nested
  class Get {

    private static String ENDPOINT_GET = "/api/v1/points";

    @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
    @Test
    void returnSavingPoint_whenConfirmPoint() {
      //arrange
      String userId = "test";
      int point = 5000;
      userJpaRepository.save(new UserModel(userId, "test@test.com", "2020-01-01", "M"));
      pointJpaRepository.save(new PointModel(userId, point));

      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);

      //act
      ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
      };
      ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
          testRestTemplate.exchange(ENDPOINT_GET, HttpMethod.GET, new HttpEntity<>(headers), responseType);
      //assert

      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().userId()).isEqualTo(userId),
          () -> assertThat(response.getBody().data().point()).isEqualTo(point)

      );
    }

    @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
    @Test
    void throwsBadRequest_whenConfirmPoint() {
      //arrange

      //act
      ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
      };
      ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
          testRestTemplate.exchange(ENDPOINT_GET, HttpMethod.GET, new HttpEntity<>(null), responseType);
      //assert

      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }
  }

  @DisplayName("POST /api/v1/points/charge")
  @Nested
  class Charge {
    private static String ENDPOINT_CHARGE = "/api/v1/points/charge";

    @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void returnSavingTotalPoint_whenCharging1000Point(int point) {
      //arrange
      String userId = "test";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);

      userJpaRepository.save(new UserModel(userId, "test@test.com", "2020-01-01", "M"));
      pointJpaRepository.save(new PointModel(userId, point));

      PointV1Dto.ChargeRequest request = new PointV1Dto.ChargeRequest(1000);

      //act
      ParameterizedTypeReference<ApiResponse<PointV1Dto.ChargeResponse>> responseType = new ParameterizedTypeReference<>() {
      };
      ResponseEntity<ApiResponse<PointV1Dto.ChargeResponse>> response =
          testRestTemplate.exchange(ENDPOINT_CHARGE, HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

      //assert
      assertAll(() -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().data().point()).isEqualTo(point + 1000));
    }

    @DisplayName("존재하지 않는 유저로 요청할 경우, 404 Not Found 응답을 반환한다.")
    @Test
    void throwsNotFoundException_whenRequestingNonExistentUser() {
      //arrange
      String userId = "not";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);

      PointV1Dto.ChargeRequest request = new PointV1Dto.ChargeRequest(100);

      //act
      ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
      };
      ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
          testRestTemplate.exchange(ENDPOINT_CHARGE, HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

      //assert
      assertAll(() -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)
      );
    }
  }

}
