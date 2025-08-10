package com.loopers.domain.user.embeded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Email {
  private String email;

  public Email() {
  }

  public Email(String email) {
    if (!email.matches("^[a-z]+@[a-z]+\\.[a-z]{2,}$")) {
      throw new IllegalArgumentException("현재 등록된 이메일 패턴과 다릅니다.");
    }
    this.email = email;
  }
}
