package com.loopers.support.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConverter {
  private final ObjectMapper objectMapper;

  public String convert(Object payload) {
    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception e) {
      throw new CoreException(ErrorType.INTERNAL_ERROR, e.getMessage());
    }

  }
}
