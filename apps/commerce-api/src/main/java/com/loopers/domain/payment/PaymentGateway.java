package com.loopers.domain.payment;

import com.loopers.application.payment.PaymentRequest;
import com.loopers.application.payment.PaymentResponse;
import com.loopers.interfaces.api.ApiResponse;

public interface PaymentGateway {

  ApiResponse<PaymentResponse> send(String userId, PaymentRequest paymentRequest);

  ApiResponse<OrderResponse> get(String userId, String orderId);
  ApiResponse<OrderResponse> get(String orderId);
}
