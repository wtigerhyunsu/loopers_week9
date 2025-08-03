package com.loopers.infrastructure.catalog.brand;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.brand.BrandRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {
  private final BrandJpaRepository repository;


  public BrandModel get(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당하는 브랜드가 존재하지 않습니다."));
  }

}
