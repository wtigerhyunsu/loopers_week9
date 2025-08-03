package com.loopers.interfaces.api.like;

import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.FAIL;
import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LikeV1ApiE2ETest {

  Function<Long, String> ENDPOINT = id -> "/api/v1/like/products/" + id;
  private final TestRestTemplate testRestTemplate;
  private final DatabaseCleanUp databaseCleanUp;

  @Autowired
  public LikeV1ApiE2ETest(TestRestTemplate testRestTemplate,
                          DatabaseCleanUp databaseCleanUp) {
    this.testRestTemplate = testRestTemplate;
    this.databaseCleanUp = databaseCleanUp;
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }


  @DisplayName("POST /api/v1/like/products/{productId}")
  @Nested
  class Like {
    @DisplayName("프로젝트 아이디가 존재하지 않는 다면, `404 NotFoundException`를 반환한다.")
    @Test
    void throw404NotFoundException_whenNotExitsProjectId() {
      //given
      Long id = null;
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-User-Id", "userId");

      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("계정 아이디가 존재하지 않는 다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      Long id = 1L;
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("특정 프로젝트에 좋아요를 누른다면, 좋아요 상태 true를 반환한다.")
    @Test
    void returnLikeStatusResponse_whenPushLike() {
      //given
      Long id = 1L;
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-User-Id", "userId");
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.POST, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().status()).isEqualTo(true),
          () -> assertThat(response.getBody().data().productId()).isEqualTo(id)

      );
    }
  }

  @DisplayName("Delete /api/v1/like/products/{productId}")
  @Nested
  class UnLike {
    @DisplayName("프로젝트 아이디가 존재하지 않는 다면, `404 NotFoundException`를 반환한다.")
    @Test
    void throw404NotFoundException_whenNotExitsProjectId() {
      //given
      Long id = null;
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-User-Id", "userId");

      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("계정 아이디가 존재하지 않는 다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      Long id = 1L;
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.DELETE, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("특정 프로젝트에 좋아요 해제를 누른다면, 좋아요 상태 false를 반환한다.")
    @Test
    void returnUnlikeStatusResponse_whenPushLike() {
      //given
      Long id = 1L;
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-User-Id", "userId");
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT.apply(id);

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().status()).isEqualTo(false),
          () -> assertThat(response.getBody().data().productId()).isEqualTo(id)

      );
    }
  }


  @DisplayName("GET /api/v1/like/products")
  @Nested
  class Get {
    String ENDPOINT = "/api/v1/like/products";

    @DisplayName("계정 아이디가 존재하지 않는 다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Register.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      ResponseEntity<ApiResponse<LikeV1Dto.Register.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("특정 계정 아이디로 조회하는 경우, 좋아요를 누른 상품 목록이 조회가 됩니다.")
    @Test
    void returnProductList_whenSearchUserId() {
      //given
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-User-Id", "userId");
      //when
      ParameterizedTypeReference<ApiResponse<LikeV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };


      ResponseEntity<ApiResponse<LikeV1Dto.Get.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().contents()).isNotEmpty()

      );
    }
  }

}
