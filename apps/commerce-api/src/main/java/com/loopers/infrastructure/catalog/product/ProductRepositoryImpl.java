package com.loopers.infrastructure.catalog.product;

import com.loopers.domain.catalog.brand.QBrandModel;
import com.loopers.domain.catalog.product.ProductCriteria;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductProjection;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.QProductModel;
import com.loopers.domain.catalog.product.QProductProjection;
import com.loopers.domain.catalog.product.status.ProductStatus;
import com.loopers.domain.catalog.product.status.QProductStatus;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
  private final JPAQueryFactory query;
  private final QProductModel product = QProductModel.productModel;
  private final QBrandModel brand = QBrandModel.brandModel;
  private final QProductStatus status = QProductStatus.productStatus;

  // 브랜드에서 해당 프로젝트 조회시
  public List<ProductModel> listOf(Long brandId) {
    return query.select(product)
        .from(product)
        .where(product.brandId.eq(brandId)).fetch();
  }

  // 검색
  public Page<ProductProjection> search(ProductCriteria criteria, Pageable pageable) {
    Long brandId = criteria.brandId();
    String productName = criteria.productName();
    String brandName = criteria.brandName();
    SortOption sort = SortOption.valueOf(criteria.sort());

    // where 절
    BooleanBuilder where = new BooleanBuilder();
    if (brandId != null) {
      where.and(product.brandId.eq(brandId));
    }
    if (brandName != null) {
      where.and(brand.name.name.contains(brandName));
    }

    if (productName != null) {
      where.and(product.name.name.contains(productName));
    }
    if (brandName != null) {
      where.and(brand.name.name.contains(brandName));
    }
    // content
    List<ProductProjection> content = query.select(new QProductProjection
            (product.id,
                product.brandId,
                brand.name.name,
                product.name.name,
                product.price.price,
                product.description,
                status.likeCount,
                product.createdAt,
                product.updatedAt))
        .from(product)
        .leftJoin(brand).on(product.brandId.eq(brand.id))
        .leftJoin(status).on(status.productId.eq(product.id))
        .where(where)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(
            switch (sort) {
              case LATEST -> product.createdAt.desc();
              case PRICE_ASC -> product.price.price.asc();
              case LIKES_DESC -> status.likeCount.desc();
            })
        .fetch();

    // 갯수
    Long totalCount = query.select(product.count())
        .from(product)
        .leftJoin(brand).on(product.brandId.eq(brand.id))
        .where(where)
        .fetchOne();

    Long total = Optional.ofNullable(totalCount).orElse(0L);

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public ProductProjection get(Long productId) {

    if (productId == null) {
      throw new CoreException(ErrorType.NOT_FOUND, "상품 ID가 존재하지 않습니다.");
    }

    return Optional.ofNullable(
            query.select(new QProductProjection
                    (product.id,
                        product.brandId,
                        brand.name.name,
                        product.name.name,
                        product.price.price,
                        product.description,
                        status.likeCount,
                        product.createdAt,
                        product.updatedAt))
                .from(product)
                .leftJoin(brand).on(product.brandId.eq(brand.id))
                .leftJoin(status).on(status.productId.eq(product.id))
                .where(product.id.eq(productId))
                .fetchOne())
        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품 ID가 존재하지 않습니다."));
  }

  @Override
  public List<ProductModel> getIn(List<Long> productIds) {
    return query.select(product)
        .from(product)
        .where(product.id.in(productIds))
        .fetch();
  }

  @Override
  public Optional<ProductStatus> has(Long productId) {
    return Optional.ofNullable(query.select(status)
        .from(status)
        .where(status.productId.eq(productId))
        .fetchOne());
  }

  @Override
  public Optional<ProductStatus> hasWithLock(Long productId) {
    return Optional.ofNullable(query.select(status)
        .from(status)
        .where(status.productId.eq(productId))
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .fetchOne());
  }


  enum SortOption {
    LATEST, PRICE_ASC, LIKES_DESC
  }
}
