package com.loopers.domain.user.embeded;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class UserId {
  private String userId;

  public UserId() {
  }

  public UserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 공백으로 생성할 수 없습니다.");
    }

    if (!userId.matches("^[A-Za-z0-9]+$")) {
      throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 숫자 혹은 알파벳으로만 만들수 있습니다.");
    }

    if (userId.length() > 10) {
      throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 10자를 초과할 수 없습니다.");
    }
    this.userId = userId;
  }

}
