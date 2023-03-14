package com.sep.coffeemanagement.dto.internal_user;

import com.sep.coffeemanagement.constant.TypeValidation;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUserReq {
  @Pattern(regexp = TypeValidation.USERNAME, message = "invalid login name")
  private String loginName;

  @Pattern(regexp = TypeValidation.PASSWORD, message = "invalid password")
  private String passWord;

  @Pattern(regexp = TypeValidation.PHONE, message = "invalid phone number")
  private String phoneNumber;
}
