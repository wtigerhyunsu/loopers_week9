package com.loopers.application.catalog.product.warmup;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.application.catalog.product.ProductCommand;
import com.loopers.application.catalog.product.ProductWarmupProcessor;
import com.loopers.application.catalog.product.SortOption;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.cache.ProductCacheRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/test-fixture.sql")
class ProductWarmupProcessorTest {

  @Autowired
  private ProductCacheRepository productCacheRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductWarmupProcessor productWarmupProcessor;


  @Test
  @DisplayName("좋아요 순위를 warm up을 하면, 캐시 정보에 저장이 되어집니다.")
  void returnCacheInfo_whenStatedLikeLankWarmup() {
    String key = "product:rank:10";
    //given
    ProductCommand productCommand = ProductCommand.builder()
        .currentPage(0)
        .perSize(10)
        .sort(SortOption.LIKES_DESC)
        .build();
    PageRequest request = PageRequest.of(0, 10);
    productRepository.search(productCommand.toCriteria(), request);
    //when
    productWarmupProcessor.warmup();
    //then
    assertThat(productCacheRepository.get(key)).isNotEmpty();
  }
}
