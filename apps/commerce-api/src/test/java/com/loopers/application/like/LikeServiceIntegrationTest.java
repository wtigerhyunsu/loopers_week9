package com.loopers.application.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import com.loopers.domain.catalog.brand.BrandModel;
import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.status.ProductStatus;
import com.loopers.domain.like.LikeModel;
import com.loopers.infrastructure.catalog.brand.BrandJpaRepository;
import com.loopers.infrastructure.catalog.product.ProductJpaRepository;
import com.loopers.infrastructure.catalog.product.status.ProductStatusJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class LikeServiceIntegrationTest {
  @Autowired
  private LikeFacade likeFacade;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private BrandJpaRepository brandJpaRepository;

  @Autowired
  private ProductJpaRepository productJpaRepository;

  @Autowired
  private ProductStatusJpaRepository productStatusJpaRepository;

  @Autowired
  private LikeJpaRepository likeRepository;

  @MockitoBean
  private LikeEventPublisher publisher;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @BeforeEach
  void init() {
    BrandModel brandModel = brandJpaRepository.save(new BrandModel("userId", "루퍼스"));
    ProductModel productModel = productJpaRepository.save(new ProductModel(brandModel.getId(), "상품1", BigInteger.valueOf(2000), "ㅇㅇ"));
    productStatusJpaRepository.save(ProductStatus.register(productModel.getId()));

  }

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }


  @DisplayName("집계가 실패하여도, 좋아요 증가 클릭은 정상적으로 되어진다.")
  @Test
  void returnLikeIncreaseUpdate_whenFailedToAggregate() {
    //given
    String userId = "userId";
    Long productId = 1L;
    //when
    likeFacade.like(userId, productId);

    doThrow(new RuntimeException("강제 예외")).when(publisher).increase(userId, productId);
    Optional<LikeModel> like = likeRepository.findByUserIdAndProductId(userId, productId);
    //then
    assertThat(like).isPresent();
  }


  @DisplayName("집계가 실패하여도, 좋아요 감소 클릭은 정상적으로 되어진다.")
  @Test
  void returnLikeDecreaseUpdate_whenFailedToAggregate() {
    //given
    String userId = "userId";
    Long productId = 1L;
    //when
    likeFacade.like(userId, productId);
    likeFacade.unlike(userId, productId);
    doThrow(new RuntimeException("강제 예외")).when(publisher).decrease(userId, productId);
    Optional<LikeModel> like = likeRepository.findByUserIdAndProductId(userId, productId);
    //then
    assertThat(like).isEmpty();
  }



  @DisplayName("최초로 좋아요를 하는 경우, 좋아요 횟수는 1이 증가한다.")
  @Test
  void returnLikeCountOne_whenFirstTimeLikeIsClicked() {
    //given
    String userId = "userId";
    Long productId = 1L;
    //when
    likeFacade.like(userId, productId);
    ProductStatus productStatus = productRepository.has(productId).get();
    //then
    assertThat(productStatus.getLikeCount()).isEqualTo(1);
  }


  @DisplayName("좋아요를 누르고 해제를 하는 경우, 좋아요 갯수는 0개가 됩니다.")
  @Test
  void returnLikedCountZero_whenUnlikedAfterLiked() {
    //given
    String userId = "userId";
    Long productId = 1L;
    //when
    likeFacade.like(userId, productId);
    likeFacade.unlike(userId, productId);
    ProductStatus productStatus = productRepository.has(productId).get();
    //then
    assertThat(productStatus.getLikeCount()).isEqualTo(0);
  }

  @DisplayName("같은 사용자의 중복 like 요청 후 unlike 하면, 좋아요 수는 0이다.")
  @Test
  void returnLikeCountZero_whenRepeatedLikesThenUnlike() {
    //given
    String userId = "userId";
    Long productId = 1L;
    //when
    likeFacade.like(userId, productId);
    likeFacade.like(userId, productId);
    likeFacade.like(userId, productId);
    likeFacade.unlike(userId, productId);
    likeFacade.unlike(userId, productId);
    likeFacade.unlike(userId, productId);
    ProductStatus productStatus = productRepository.has(productId).get();
    //then
    assertThat(productStatus.getLikeCount()).isEqualTo(0);
  }

  @DisplayName("동시성 테스트")
  @Nested
  class Concurrency {

    @DisplayName("200명이 동시에 좋아요를 누르는경우, 상품 좋아요 갯수는 200개를 리턴한다.")
    @Test
    void concurrencyTest_LikeShouldBeProperlyIncreasedWhenLiked() throws InterruptedException {
      int threadCount = 200;
      ExecutorService executor = Executors.newFixedThreadPool(threadCount);
      CountDownLatch latch = new CountDownLatch(threadCount);

      for (int i = 0; i < threadCount; i++) {
        String userId = "userId" + i;
        executor.submit(() -> {
          try {
            likeFacade.like(userId, 1L);
          } catch (Exception e) {
            System.out.println("실패: " + e.getMessage());
          } finally {
            latch.countDown();
          }
        });
      }

      latch.await();

      ProductStatus productStatus = productRepository.has(1L).get();
      assertThat(productStatus.getLikeCount()).isEqualTo(threadCount);
    }

    @DisplayName("200명이 동시에 좋아요 or 해제를 누르는경우, 상품 좋아요 갯수는 0개를 리턴한다.")
    @Test
    void concurrencyTest_LikeShouldBeProperlyIncreasedWhenLikedOrDisLiked() throws InterruptedException {
      int threadCount = 200;
      ExecutorService executor = Executors.newFixedThreadPool(threadCount);
      CountDownLatch latch = new CountDownLatch(threadCount);

      for (int i = 0; i < threadCount; i++) {
        String userId = "userId" + i;
        executor.submit(() -> {
          try {
            likeFacade.like(userId, 1L);
            likeFacade.unlike(userId, 1L);
          } catch (Exception e) {
            System.out.println("실패: " + e.getMessage());
          } finally {
            latch.countDown();
          }
        });
      }

      latch.await();

      ProductStatus productStatus = productRepository.has(1L).get();
      assertThat(productStatus.getLikeCount()).isEqualTo(0);
    }
  }
}
