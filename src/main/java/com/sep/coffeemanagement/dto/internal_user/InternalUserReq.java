package com.sep.coffeemanagement.dto.internal_user;

import com.sep.coffeemanagement.constant.TypeValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

  @Pattern(regexp = TypeValidation.PHONE, message = "invalid phone number")
  private String phoneNumber;

  @Pattern(regexp = TypeValidation.EMAIL, message = "invalid email")
  private String email;

  @NotEmpty@NotBlank@NotNull(message = "address is null ")
  private String address;
}
