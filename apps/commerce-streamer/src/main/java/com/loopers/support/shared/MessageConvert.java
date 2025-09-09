package com.loopers.support.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageConvert {
  private final ObjectMapper objectMapper;

  public MessageConvert(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T convert(String payload, Class<T> clazz) {

    try {
      // 1차 시도: 직접 파싱
      return objectMapper.readValue(payload, clazz);
    } catch (Exception e) {

      try {
        // 2차 시도: String으로 한번 더 파싱 (이중 인코딩된 경우)
        String unescaped = objectMapper.readValue(payload, String.class);
        return objectMapper.readValue(unescaped, clazz);
      } catch (Exception fallbackException) {

        try {
          // 3차 시도: 앞뒤 따옴표 제거 후 파싱
          if (payload.length() > 2 && payload.startsWith("\"") && payload.endsWith("\"")) {
            String unquoted = payload.substring(1, payload.length() - 1);
            // 이스케이프 문자 제거
            String cleaned = unquoted.replace("\\\"", "\"").replace("\\\\", "\\");
            return objectMapper.readValue(cleaned, clazz);
          }
        } catch (Exception cleanException) {
        }
        
        throw new IllegalArgumentException("Failed to parse JSON: " + payload, e);
      }
    }
  }
}
