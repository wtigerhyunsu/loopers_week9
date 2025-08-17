package com.loopers.interfaces.api.product;

import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.FAIL;
import static com.loopers.interfaces.api.ApiResponse.Metadata.Result.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.status.ProductStatus;
import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.infrastructure.catalog.brand.BrandJpaRepository;
import com.loopers.infrastructure.catalog.product.ProductJpaRepository;
import com.loopers.infrastructure.catalog.product.status.ProductStatusJpaRepository;
import com.loopers.infrastructure.catalog.product.stock.StockJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
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
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductModelV1ControllerTest {


  private static final String ENDPOINT = "/api/v1/products";
  private final TestRestTemplate testRestTemplate;
  private final DatabaseCleanUp databaseCleanUp;

  private final BrandJpaRepository brandRepository;
  private final ProductJpaRepository productRepository;
  private final StockJpaRepository stockRepository;
  private final ProductStatusJpaRepository productStatusJpaRepository;


  @Autowired
  public ProductModelV1ControllerTest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp,
                                      BrandJpaRepository brandRepository, ProductJpaRepository productRepository,
                                      StockJpaRepository stockRepository,
                                      ProductStatusJpaRepository productStatusJpaRepository) {
    this.testRestTemplate = testRestTemplate;
    this.databaseCleanUp = databaseCleanUp;
    this.brandRepository = brandRepository;
    this.productRepository = productRepository;
    this.stockRepository = stockRepository;
    this.productStatusJpaRepository = productStatusJpaRepository;
  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("GET /api/v1/products")
  @Nested
  class List {
    @DisplayName("필터링 조건을 지정하지 않는다면, 모든 상품을 리스토로 리턴합니다.")
    @Test
    public void returnAllProducts_whenNonFiltering() {
      //given
      BrandModel brandModel = brandRepository.save(new BrandModel("userId", "브랜드 명"));

      ProductModel productModel1 = productRepository.save(new ProductModel(brandModel.getId(), "상품1", BigInteger.valueOf(2000), "상품 설명"));
      ProductModel productModel2 = productRepository.save(new ProductModel(brandModel.getId(), "상품2", BigInteger.valueOf(20000), "상품 설명2"));
      ProductModel productModel3 = productRepository.save(new ProductModel(brandModel.getId(), "상품3", BigInteger.valueOf(3000), "상품 설명2"));

      stockRepository.save(new StockModel(productModel1.getId(), 100L));
      stockRepository.save(new StockModel(productModel2.getId(), 200L));
      stockRepository.save(new StockModel(productModel3.getId(), 300L));

      productStatusJpaRepository.save(ProductStatus.of(productModel1.getId(), 0));
      productStatusJpaRepository.save(ProductStatus.of(productModel2.getId(), 0));
      productStatusJpaRepository.save(ProductStatus.of(productModel3.getId(), 0));
      //when
      ParameterizedTypeReference<ApiResponse<ProductV1Dto.Search.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String url = UriComponentsBuilder.fromUriString(ENDPOINT)
          .toUriString();

      ResponseEntity<ApiResponse<ProductV1Dto.Search.Response>> response =
          testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), responseType);
      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS)
      );
    }
  }

  @DisplayName("GET /api/v1/products/{productId}")
  @Nested
  class Get {
    Function<Long, String> ENDPOINT_GET = id -> ENDPOINT + "/" + id;


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
      BrandModel brandModel = brandRepository.save(new BrandModel("userId", "브랜드 명"));

      ProductModel productModel = productRepository.save(new ProductModel(brandModel.getId(), "상품1", BigInteger.valueOf(2000), "상품 설명"));

      stockRepository.save(new StockModel(productModel.getId(), 100L));

      productStatusJpaRepository.save(ProductStatus.of(productModel.getId(), 0));

      //when
      ParameterizedTypeReference<ApiResponse<ProductV1Dto.Get.Response>> responseType = new ParameterizedTypeReference<>() {
      };

      String endpoint = ENDPOINT_GET.apply(productModel.getId());

      ResponseEntity<ApiResponse<ProductV1Dto.Get.Response>> response =
          testRestTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(null), responseType);

      //then
      assertAll(
          () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
          () -> assertThat(response.getBody().meta().result()).isEqualTo(SUCCESS),
          () -> assertThat(response.getBody().data().productId()).isEqualTo(1L)
      );
    }

  }
}
