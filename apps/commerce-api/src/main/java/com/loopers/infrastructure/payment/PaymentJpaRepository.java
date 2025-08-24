package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentStatus;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentJpaRepository extends JpaRepository<PaymentModel, Long> {

  Optional<PaymentModel> findByTransactionId(String transactionId);

  @Query("""
      SELECT p FROM PaymentModel p WHERE p.status = :status AND p.createdAt <= :threshold
      """)
  List<PaymentModel> findPendingOlderThan(@Param("status") PaymentStatus status,
                                          @Param("threshold") ZonedDateTime threshold);


  @Modifying
  @Query("update PaymentModel p set p.status = :paymentStatus where p.orderNumber = :orderId")
  void updateStatus(@Param("orderId") String orderId, @Param("paymentStatus") PaymentStatus paymentStatus);

  @Query("SELECT p FROM PaymentModel p WHERE p.orderNumber = :orderNumber")
  List<PaymentModel> findAllByOrderNumber(@Param("orderNumber") String orderNumber);
}
