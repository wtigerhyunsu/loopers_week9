package com.loopers.infrastructure.payment;

import com.loopers.application.payment.PaymentRequest;
import com.loopers.application.payment.PaymentResponse;
import com.loopers.domain.payment.OrderResponse;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-gateway", url = "localhost:8082/api/v1/payments"
,configuration = PaymentClientConfig.class)
public interface PaymentGatewayClient {


  @PostMapping()
  ApiResponse<PaymentResponse> send(@RequestHeader("X-USER-ID") String userId,
                                    @RequestBody PaymentRequest paymentRequest);


  @GetMapping
  ApiResponse<OrderResponse> get(@RequestHeader("X-USER-ID") String userId,
                                 @RequestParam(name = "orderId", required = false) String orderId);

}
