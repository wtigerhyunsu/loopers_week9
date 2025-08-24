package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.payment.PaymentV1Dto.CreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentV1Controller {
  private final PaymentFacade paymentFacade;


  @PostMapping()
  public ApiResponse<CreateResponse> create(
      @RequestHeader("X-USER-ID") String userId,
      @RequestBody PaymentV1Dto.Create request) {
    return ApiResponse.success(new CreateResponse(paymentFacade.payment(request.toCommand(userId))));
  }


  @PostMapping("/callback")
  public void callback(@RequestBody PaymentV1Dto.Callback request) {
    paymentFacade.callback(request.toCommand());
  }
}
