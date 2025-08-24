package com.loopers.interfaces.api.order;

import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.FAIL;
import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.infrastructure.catalog.brand.BrandJpaRepository;
import com.loopers.infrastructure.catalog.product.ProductJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.Create.OrderItemRequest;
import com.loopers.interfaces.api.order.OrderV1Dto.Create.Request;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class OrderV1ApiE2ETest {
  private static final String ENDPOINT = "/api/v1/orders";
  private final TestRestTemplate testRestTemplate;
  private final DatabaseCleanUp databaseCleanUp;


  private final ProductJpaRepository productJpaRepository;
  private final BrandJpaRepository brandJpaRepository;

  @Autowired
  public OrderV1ApiE2ETest(TestRestTemplate testRestTemplate, BrandJpaRepository brandJpaRepository, ProductJpaRepository productJpaRepository, DatabaseCleanUp databaseCleanUp) {
    this.testRestTemplate = testRestTemplate;
    this.brandJpaRepository = brandJpaRepository;
    this.productJpaRepository = productJpaRepository;
    this.databaseCleanUp = databaseCleanUp;
  }

  @BeforeEach
  void setUp() {
    BrandModel brandModel = brandJpaRepository.save(new BrandModel("userId", "브랜드1"));
    productJpaRepository.save(new ProductModel(brandModel.getId(),"상품1", BigInteger.valueOf(200),"zzz"));
    productJpaRepository.save(new ProductModel(brandModel.getId(),"상품2", BigInteger.valueOf(200),"zzz"));
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("POST /api/v1/orders")
  @Nested
  class Create {

    @DisplayName("계정 아이디가 존재하지 않는다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Create.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      ResponseEntity<ApiResponse<OrderV1Dto.Create.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("주문을 생성하면 주문 정보를 리턴합니다.")
    @Test
    void returnOrderInfo_whenCreatedOrder() {
      //given
      String userId = "test";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);

      Long productId1 = 1L;
      Long quantity1 = 2L;

      Long productId2 = 2L;
      Long quantity2 = 2L;

      Request request = new Request(
          "aaa",
          List.of(new OrderItemRequest(productId1, quantity1),
              new OrderItemRequest(productId2, quantity2)),
          "aaaa"
      );

      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Create.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      ResponseEntity<ApiResponse<OrderV1Dto.Create.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().userId()).isEqualTo(userId)

      );
    }

  }

  @DisplayName("GET /api/v1/orders")
  @Nested
  class Search {
    @DisplayName("계정 아이디가 존재하지 않는다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Create.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      ResponseEntity<ApiResponse<OrderV1Dto.Create.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("계정 아이디가 존재한다면, 그 계정이 주문한 주문 목록을 리턴합니다.")
    @Test
    void returnOrderList_whenExitsUserId() {
      //given
      String userId = "test";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Create.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      ResponseEntity<ApiResponse<OrderV1Dto.Create.Response>> response =
          testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS)

      );
    }
  }

  @Nested
  @DisplayName("GET /api/v1/orders/{orderId}")
  class Get {
    Function<Long, String> ENDPOINT = id -> "/api/v1/orders/" + id;


    @DisplayName("계정 아이디가 존재하지 않는다면, `401 Unauthorized`를 반환한다.")
    @Test
    void throw401UnauthorizedException_whenNotExitsUserId() {
      //given
      Long orderId = 1L;
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endPoint = ENDPOINT.apply(orderId);

      ResponseEntity<ApiResponse<OrderV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endPoint, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("주문 아이디가 존재하지 않는다면, `404 NotFound`를 반환한다.")
    @Test
    void throw404NotFoundException_whenNotExitsOrderId() {
      //given
      Long orderId = null;
      String userId = "test";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endPoint = ENDPOINT.apply(orderId);

      ResponseEntity<ApiResponse<OrderV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endPoint, HttpMethod.GET, new HttpEntity<>(headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is4xxClientError()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(FAIL)

      );
    }

    @DisplayName("주문를 상세 조회하는 경우, 주문 정보를 리턴한다.")
    @Test
    void returnOrderInfo_whenSearchOrderDetail() {
      //given
      Long orderId = 1L;
      String userId = "test";
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-USER-ID", userId);
      //when
      ParameterizedTypeReference<ApiResponse<OrderV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endPoint = ENDPOINT.apply(orderId);

      ResponseEntity<ApiResponse<OrderV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endPoint, HttpMethod.GET, new HttpEntity<>(headers), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().orderId()).isEqualTo(orderId)

      );
    }

  }
}
