package com.loopers.domain.brand;

import com.loopers.domain.catalog.brand.embeded.Products;
import com.loopers.domain.catalog.product.ProductModel;
import java.math.BigInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BrandModelModelTest {

  @DisplayName("브랜드에 상품을,")
  @Nested
  class ProductModelList {

    @Test
    @DisplayName("추가하지 않고, 초기 상태인 경우, 상품 리스트의 길이는 0이다.")
    void thenProductListSizeIsZero_whenInitialProductInBrand() {
      //given
      Products products = Products.of();
      //when
      int size = products.getProducts().size();
      //then
      Assertions.assertThat(size).isEqualTo(0);
    }

    @Test
    @DisplayName("추가하는 경우, 상품 리스트의 길이가 1증가 되어진다.")
    void thenProductListSizePlus1_whenAddProductInBrand() {
      //given
      Products products = Products.of();
      ProductModel product = new ProductModel(1L,"상품1", BigInteger.valueOf(200),"상품11");
      //when
      products.add(product);
      //then
      Assertions.assertThat(products.getProducts().size()).isEqualTo(1);
    }
  }
}
