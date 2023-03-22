package com.sep.coffeemanagement.dto.internal_user_register;

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
public class InternalUserRegisterReq {
  @Pattern(regexp = TypeValidation.USERNAME, message = "invalid username")
  private String registerName;

  @NotNull(message = "register password is null")
  private String registerPassword;

  @Pattern(regexp = TypeValidation.EMAIL, message = "email is not valid")
  private String email;
}
