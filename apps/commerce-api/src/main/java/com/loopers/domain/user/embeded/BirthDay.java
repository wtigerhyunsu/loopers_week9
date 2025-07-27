package com.loopers.domain.user.embeded;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import lombok.Getter;

@Embeddable
@Getter
public class BirthDay {
  private LocalDate birthday;

  public BirthDay() {}

  public BirthDay(String birthday) {
    if (!isValidBirth(birthday)) {
      throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식은 yyyy-MM-dd 이어야 하며, 유효한 날짜여야 합니다.");
    }
    this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

  private boolean isValidBirth(String birthday) {
    DateTimeFormatter BIRTH_FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd")
        .withResolverStyle(ResolverStyle.STRICT);

    if (birthday == null) {
      return false;
    }
    try {
      LocalDate.parse(birthday, BIRTH_FMT);
      return true;
    } catch (DateTimeParseException ex) {
      return false;
    }
  }
}
