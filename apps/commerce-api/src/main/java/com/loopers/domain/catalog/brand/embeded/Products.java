package com.loopers.domain.catalog.brand.embeded;


import com.loopers.domain.catalog.product.ProductModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Products {
  private List<ProductModel> products;

  protected Products() {
  }

  private Products(List<ProductModel> products) {
    this.products = products;
  }

  public static Products of() {
    return new Products(new ArrayList<>());
  }

  public static Products of(List<ProductModel> products) {
    return new Products(products);
  }

  public void add(ProductModel product) {
    this.products.add(product);
  }

}
