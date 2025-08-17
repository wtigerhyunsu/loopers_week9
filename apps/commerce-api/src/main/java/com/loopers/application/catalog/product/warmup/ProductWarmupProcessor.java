package com.loopers.application.catalog.product.warmup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.application.catalog.product.ProductCommand;
import com.loopers.application.catalog.product.SortOption;
import com.loopers.domain.catalog.product.ProductProjection;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.cache.ProductCacheRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductWarmupProcessor {
  private final ProductCacheRepository cacheRepository;
  private final ProductRepository productRepository;

  private final String KEY = "product:rank:10";

  public void warmup() {
    ObjectMapper objectMapper = new ObjectMapper();
    String productRankValue = cacheRepository.get(KEY);
    if (productRankValue != null) {
      log.info("TTL 강제 초기화");
      cacheRepository.remove(KEY);
    }

    ProductCommand command = ProductCommand.builder()
        .perSize(10)
        .currentPage(0)
        .sort(SortOption.LIKES_DESC)
        .build();

    Page<ProductProjection> search = productRepository.search(command.toCriteria(), PageRequest.of(0, 10));
    String value;
    try {
      value = objectMapper.writeValueAsString(search.getContent());
    } catch (JsonProcessingException e) {
      throw new CoreException(ErrorType.INTERNAL_ERROR, "json parse error : " + e.getMessage());
    }

    log.info("warm up data : {}", value);
    cacheRepository.put(KEY, value, Duration.ofDays(1));
  }

  public Page<ProductProjection> searchData(ProductCommand command) {
    PageRequest page = PageRequest.of(command.currentPage(), command.perSize());
    // 1. 캐시 사용 조건(좋아요순 정렬)이면서 캐시에 값이 실제로 존재할 때만 캐시 로직 실행
    if (command.sort() == SortOption.LIKES_DESC) {
      try {
        String value = cacheRepository.get(KEY);
        if (value != null) {
          log.info("cache hit");
          try {
            List<ProductProjection> cacheContent = new ObjectMapper().readValue(value, new TypeReference<>() {
            });
            // 캐시 처리 성공 시, 여기서 결과를 바로 반환하고 메서드를 종료합니다.
            return new PageImpl<>(cacheContent, Pageable.ofSize(10), 10);
          } catch (JsonProcessingException e) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, "json parse error : " + e.getMessage());
          }
        }
        // '좋아요순'이지만 캐시에 값이 없는 경우 (cache miss)
        log.info("cache miss");
      } catch (Exception ignore) {
      }
    }
    return productRepository.search(command.toCriteria(), page);
  }
}
