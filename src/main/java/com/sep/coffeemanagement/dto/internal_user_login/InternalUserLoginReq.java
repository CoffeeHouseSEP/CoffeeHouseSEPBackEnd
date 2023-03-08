package com.sep.coffeemanagement.dto.internal_user_login;

import com.sep.coffeemanagement.constant.TypeValidation;
import javax.validation.Validation;
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
public class InternalUserLoginReq {
  @Pattern(regexp = TypeValidation.USERNAME, message = "invalid username")
  private String loginName;

  @NotNull
  @NotBlank
  @NotEmpty
  private String loginPassword;
}
