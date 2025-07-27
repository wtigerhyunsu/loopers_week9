package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.embeded.BirthDay;
import com.loopers.domain.user.embeded.Email;
import com.loopers.domain.user.embeded.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "user")
public class UserModel extends BaseEntity {

  @Embedded
  private UserId userId;

  @Embedded
  private Email email;

  @Embedded
  private BirthDay birthday;

  private String gender;

  public UserModel() {
  }

  public UserModel(String userId, String email, String birthday, String gender) {
    this.userId = new UserId(userId);
    this.email = new Email(email);
    this.birthday = new BirthDay(birthday);
    this.gender = gender;
  }


  public String getUserId() {
    return userId.getUserId();
  }

  public String getEmail() {
    return email.getEmail();
  }

  public LocalDate getBirthday() {
    return birthday.getBirthday();
  }


  public String getGender() {
    return gender;
  }

}
