package com.loopers.application.catalog.product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.product.ProductProjection;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.infrastructure.catalog.brand.BrandJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

/**
 * 상품 정보는 SQL를 통해 미리 등록되어 있습니다.
 */

@Sql("/sql/test-fixture.sql")
@SpringBootTest
class ProductServiceIntegrationTest {

  @Autowired
  private ProductFacade productFacade;
  @Autowired
  private ProductRepository repository;

  @Autowired
  private BrandJpaRepository brandRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("목록 조회를 하는 경우")
  @Nested
  class Search {
    // 기본값이 10개
    @DisplayName("아무런 조건 없이 조회를 하는 경우, 상품 리스트 10개를 반환합니다.")
    @Test
    void returnProductList_whenSearchingNotCondition() {
      //given
      ProductCommand command = ProductCommand.builder().build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      //then
      assertThat(search).isNotNull();
      assertThat(search.contents()).hasSize(10);
      search.contents().forEach(System.out::println);
    }


    @DisplayName("콘텐츠 갯수를 13개 조회 하는 경우, 상품 리스트 13개를 반환합니다.")
    @Test
    void returnProductListSizeIs13_whenSearchingPerContentsSizeIs13() {
      //given
      ProductCommand command = ProductCommand.builder()
          .perSize(13)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      //then
      assertThat(search).isNotNull();
      assertThat(search.contents()).hasSize(13);
      search.contents().forEach(System.out::println);
    }

    @DisplayName("페이지 시작 갯수를 다르게 조회 하는 경우, 서로 다른 페이지의 내용은 서로 다릅니다.")
    @Test
    void returnDifferentPagesHaveDifferentContent() {
      //given
      ProductCommand command1 = ProductCommand.builder()
          .currentPage(0)
          .perSize(13)
          .build();
      ProductCommand command2 = ProductCommand.builder()
          .currentPage(1)
          .perSize(13)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command1);
      ProductSearchInfo search2 = productFacade.search(command2);
      //then
      assertThat(search).isNotEqualTo(search2);
    }

    @DisplayName("특정 브랜드 아이디로 조회하는 경우, 그 브랜드에 해당하는 상품 리스트가 조회됩니다.")
    @Test
    void returnProductListForBrandId_whenSearchingBrandId() {
      //given
      ProductCommand command = ProductCommand.builder()
          .brandId(1L)
          .currentPage(0)
          .perSize(3)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      //then
      List<ProductContents> content = search.contents();

      String brandName = brandRepository.findById(1L).get().getName();
      for (ProductContents productModel : content) {
        assertThat(productModel.brandName()).isEqualTo(brandName);
      }
    }


    @DisplayName("상품 명으로 조회하는 경우, 해당하는 상품 리스트가 조회됩니다.")
    @ParameterizedTest
    @ValueSource(strings = {"무선", "태블릿", "키보드", "아메리카노"})
    void returnProductList_whenSearchingProductName(String productName) {
      //given
      ProductCommand command = ProductCommand.builder()
          .productName(productName)
          .currentPage(0)
          .perSize(3)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      //then
      List<ProductContents> contents = search.contents();

      for (ProductContents content : contents) {
        assertThat(content.name()).contains(productName);
      }
    }


    @DisplayName("브랜드 명으로 조회하는 경우, 해당하는 상품 리스트가 조회됩니다.")
    @ParameterizedTest
    @ValueSource(strings = {"루", "소마", "가나", "닉스"})
    void returnProductList_whenSearchingBrandName(String brandName) {
      //given
      ProductCommand command = ProductCommand.builder()
          .brandName(brandName)
          .currentPage(0)
          .perSize(3)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      //then
      List<ProductContents> contents = search.contents();

      for (ProductContents content : contents) {
        assertThat(content.brandName()).contains(brandName);
      }
    }


    @DisplayName("가격을 기준으로 조회하는 경우, 해당하는 상품 리스트가 조회됩니다.(오름차순)")
    @Test
    void returnProductList_whenSortingPriceAsc() {
      //given
      ProductCommand command = ProductCommand.builder()
          .sort(SortOption.PRICE_ASC)
          .currentPage(0)
          .perSize(3)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      List<ProductContents> contents = search.contents();
      ProductContents preContent = contents.getFirst();
      //then

      for (int i = 1; i < contents.size(); i++) {
        ProductContents nextModel = contents.get(i);
        assertThat(preContent.price().compareTo(nextModel.price()) <= 0).isTrue();
        preContent = nextModel;
      }

    }


    @DisplayName("좋아요 갯수를 기준으로 조회하는 경우, 해당하는 상품 리스트가 조회됩니다.(내림차순)")
    @Test
    void returnProductList_whenSortingLikedDesc() {
      //given
      ProductCommand command = ProductCommand.builder()
          .sort(SortOption.LIKES_DESC)
          .currentPage(0)
          .perSize(3)
          .build();
      //when
      ProductSearchInfo search = productFacade.search(command);
      List<ProductContents> contents = search.contents();
      ProductContents preContent = contents.getFirst();
      //then
      for (int i = 1; i < contents.size(); i++) {
        ProductContents nextModel = contents.get(i);
        assertThat(preContent.likeCount()).isGreaterThanOrEqualTo(nextModel.likeCount());
        preContent = nextModel;
      }

    }

    @DisplayName("상세 조회를 하는 경우,")
    @Nested
    class Get {
      // 상품 ID가 존재하지않는 경우
      @DisplayName("상품 ID가 존재하지 않는 경우, `400 BadRequest`를 반환한다.")
      @Test
      void throw400BadRequest_whenNotExitsProductId() {
        //given
        Long productId = null;
        //when
        CoreException result = assertThrows(CoreException.class, () -> productFacade.get(null, productId));
        //then
        AssertionsForInterfaceTypes.assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
      }

      // 정상 조회
      @DisplayName("상품 ID가 존재하는 경우, 상품 정보를 리턴한다.")
      @Test
      void returnProductInfo_whenExitsProductId() {
        //given
        Long productId = 1L;
        int count = 0;
        ProductProjection productModel = repository.get(productId);
        //when
        ProductGetInfo productGetInfo = productFacade.get(null, productId);
        //then
        assertThat(productGetInfo.productId()).isEqualTo(productId);
        assertThat(productGetInfo.productName()).isEqualTo(productModel.getName());
        assertThat(productGetInfo.brandName()).isEqualTo(productModel.getBrandName());
        assertThat(productGetInfo.price()).isEqualTo(productModel.getPrice());
        assertThat(productGetInfo.likedCount()).isEqualTo(count);
        assertThat(productGetInfo.description()).isEqualTo(productModel.getDescription());
        System.out.println(productGetInfo);
      }
    }
  }

}
