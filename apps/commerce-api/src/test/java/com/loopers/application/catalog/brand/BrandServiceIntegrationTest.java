package com.loopers.application.catalog.brand;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.infrastructure.catalog.brand.BrandJpaRepository;
import com.loopers.infrastructure.catalog.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BrandServiceIntegrationTest {

  @Autowired
  private BrandJpaRepository brandRepository;
  @Autowired
  private ProductJpaRepository productRepository;
  @Autowired
  private BrandFacade brandFacade;
  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("존재하지 않는 브랜드 ID로 브랜드를 조회하는 경우, `404 NotFound Exception`이 발생한다.")
  @Test
  void throw404NotFoundException_whenNotExitsBrandId() {
    //given
    BrandModel brandModel = brandRepository.save(new BrandModel("userId", "나이키"));
    //when
    CoreException result = assertThrows(CoreException.class, () -> brandFacade.get(2L));
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @DisplayName("브랜드를 조회하시 프로젝트 정보가 존재하지 않는 경우, 상품 리스트의 크기는 0이 된다.")
  @Test
  void returnProductSizeIsZero_whenGetBrandNotExitsProductInfo() {
    //given
    BrandModel brandModel = brandRepository.save(new BrandModel("userId", "나이키"));
    //when
    BrandInfo brandInfo = brandFacade.get(1L);
    //then
    assertThat(brandInfo.products()).size().isEqualTo(0);
  }

  @DisplayName("브랜드를 조회하는 경우, 상품 리스트가 리턴이 되어진다.")
  @Test
  void returnBrandInfoInProducts_whenGetBrand() {
    //given
    BrandModel brandModel = brandRepository.save(new BrandModel("userId", "나이키"));
    ProductModel productModel1 = new ProductModel(1L, "상품1", BigInteger.valueOf(2000), "무시");
    ProductModel productModel2 = new ProductModel(1L, "상품2", BigInteger.valueOf(3000), "무시22");
    ProductModel productModel3 = new ProductModel(1L, "상품3", BigInteger.valueOf(4000), "무시464");
    List<ProductModel> productModels = List.of(productModel1, productModel2, productModel3);
    productRepository.saveAll(productModels);
    //when
    BrandInfo brandInfo = brandFacade.get(1L);
    //then
    assertThat(brandInfo.brandId()).isEqualTo(brandModel.getId());
    assertThat(brandInfo.brandName()).isEqualTo(brandModel.getName());
    List<HasProduct> products = brandInfo.products();
    assertThat(products).hasSize(3);
    assertThat(products)
        .extracting(HasProduct::productName)
        .containsExactly("상품1", "상품2", "상품3");
  }
}
