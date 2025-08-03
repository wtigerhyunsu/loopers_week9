package com.loopers.application.like;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.status.ProductStatus;
import com.loopers.domain.like.LikeRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/sql/test-fixture.sql")
class LikeServiceIntegrationTest {
  @Autowired
  private LikeFacade likeFacade;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private LikeRepository likeRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
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

}
