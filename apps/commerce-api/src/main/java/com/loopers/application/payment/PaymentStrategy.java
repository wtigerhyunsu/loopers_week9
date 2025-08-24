package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;

public interface PaymentStrategy {

  PaymentModel process(PaymentCommand command, OrderModel orderModel);

  PaymentMethod getType();
}
