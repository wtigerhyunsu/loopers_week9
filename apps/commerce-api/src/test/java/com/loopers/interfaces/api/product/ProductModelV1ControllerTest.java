package com.loopers.interfaces.api.product;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductModelV1ControllerTest {

  private static final String ENDPOINT = "/api/v1/products/";
  private final TestRestTemplate testRestTemplate;
  private final DatabaseCleanUp databaseCleanUp;

  @Autowired
  public ProductModelV1ControllerTest(
      TestRestTemplate testRestTemplate,
      DatabaseCleanUp databaseCleanUp
  ) {
    this.testRestTemplate = testRestTemplate;
    this.databaseCleanUp = databaseCleanUp;
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("GET /api/v1/products")
  @Nested
  class List {

//    @DisplayName("필터링 조건을 지정하지 않는다면, 모든 상품을 리스토로 리턴합니다.")
//    @Test
//    public void returnAllProducts_whenNonFiltering() {
//      //given
//
//      //when
//      ParameterizedTypeReference<ApiResponse<ProductV1Dto.Search.Response>> responseType = new ParameterizedTypeReference<>() {
//      };
//
//      ResponseEntity<ApiResponse<ProductV1Dto.Search.Response>> response =
//          testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null), responseType);
//      //then
//      assertAll(
//          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
//          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS)
//      );
//    }
  }

  @DisplayName("GET /api/v1/products/{productId}")
  @Nested
  class Get{
    Function<Long,String> ENDPOINT_GET = id -> ENDPOINT + id;


    @DisplayName("상품 ID가 존재하지 않는다면, 404 NotFound Exception을 반환합니다.")
    @Test
    public void return404NotFoundException_whenNotFoundProductId() {
      //given
      Long id = null;
      //when
      ParameterizedTypeReference<ApiResponse<ProductV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT_GET.apply(id);

      ResponseEntity<ApiResponse<ProductV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)
      );
    }

    @DisplayName("상품 ID가 존재한다면, 상품 정보를 반환합니다.")
    @Test
    public void returnProductInfo_whenProductId() {
      //given
      Long id = 1L;
      //when
      ParameterizedTypeReference<ApiResponse<ProductV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT_GET.apply(id);

      ResponseEntity<ApiResponse<ProductV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().id()).isEqualTo(id)
      );
    }

  }
}
