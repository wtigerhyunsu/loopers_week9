package com.loopers.interfaces.api.brand;

import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.FAIL;
import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.util.function.Function;
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
class BrandModelV1ControllerTest {

  private final TestRestTemplate testRestTemplate;
  private final DatabaseCleanUp databaseCleanUp;

  @Autowired
  public BrandModelV1ControllerTest(
      TestRestTemplate testRestTemplate,
      DatabaseCleanUp databaseCleanUp
  ) {
    this.testRestTemplate = testRestTemplate;
    this.databaseCleanUp = databaseCleanUp;
  }

  @DisplayName("GET /api/v1/brands")
  @Nested
  class Get {
    private static final Function<Long, String> ENDPOINT_GET = id -> "/api/v1/brands/" + id;
    // 브랜드가 존재하지 않을 시
    @DisplayName("브랜드 ID가 존재하지 않을시 `404 NotFound Exception`을 반환합니다.")
    @Test
    public void throws404Exception_when_not_found_brand_id() {
      // given
      Long id = null;
      // when
      ResponseEntity<ApiResponse<BrandV1Dto.Response>> response = get(id);
      // then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    // 브랜드에 프로젝트에 존재할시
    @DisplayName(" 브랜드가 존재할시 `브랜드 정보`를 리턴합니다.")
    @Test
    public void returnBrandInfo_when_exists_brand() {
      // given
      Long id = 1L;
      // when
      ResponseEntity<ApiResponse<BrandV1Dto.Response>> response = get(id);
      // then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().brandId()).isEqualTo(id)
      );

    }

    private ResponseEntity<ApiResponse<BrandV1Dto.Response>> get(Long id) {
      String endpoint = ENDPOINT_GET.apply(id);
      ParameterizedTypeReference<ApiResponse<BrandV1Dto.Response>> responseType = new ParameterizedTypeReference<>() {};
      return testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);
    }

  }

}
