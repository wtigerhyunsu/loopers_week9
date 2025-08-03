package com.loopers.infrastructure.catalog.brand;

import com.loopers.domain.catalog.brand.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<BrandModel,Long> {
}
