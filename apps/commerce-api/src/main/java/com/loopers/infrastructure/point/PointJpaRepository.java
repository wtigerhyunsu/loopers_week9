package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PointJpaRepository extends JpaRepository<PointModel, Long> {

  Optional<PointModel> findByUserId(String userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from PointModel p where p.userId = :userId")
  Optional<PointModel> findByUserIdWithLock(String userId);


  @Transactional
  @Modifying
  @Query("delete from PointModel p where p.userId=:userId")
  void deleteByUserIdForTest(String userId);
}
