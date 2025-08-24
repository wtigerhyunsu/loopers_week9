package com.loopers.application.payment;

import com.loopers.domain.payment.OrderResponse;

public interface PaymentGatewayPort {

  PaymentResponse send(PaymentGatewayCommand command);
  OrderResponse get(String orderId);
}
