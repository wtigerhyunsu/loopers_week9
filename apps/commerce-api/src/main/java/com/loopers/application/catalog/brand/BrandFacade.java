package com.loopers.application.catalog.brand;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.brand.BrandRepository;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandFacade {
  private final BrandRepository brandRepository;
  private final ProductRepository productRepository;

  public BrandInfo get(Long brandId) {
    BrandModel brandModel = brandRepository.get(brandId);
    List<ProductModel> hasProducts = productRepository.listOf(brandId);

    return BrandInfo.builder().brandName(brandModel.getName())
        .brandId(brandModel.getId())
        .products(hasProducts.stream()
            .map(product -> HasProduct.of(product.getId(),
                                                        product.getName()))
            .collect(Collectors.toList())).build();

  }
}
