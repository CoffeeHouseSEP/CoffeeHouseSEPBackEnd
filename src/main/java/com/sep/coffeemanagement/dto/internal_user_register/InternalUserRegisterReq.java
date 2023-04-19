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
  @Pattern(
    regexp = TypeValidation.USERNAME,
    message = "Tên đăng nhập không đúng định dạng"
  )
  private String registerName;

  @NotNull(message = "Mật khẩu không được để trống")
  private String registerPassword;

  @Pattern(regexp = TypeValidation.EMAIL, message = "Email không đúng định dạng")
  private String email;

  @Pattern(
    regexp = TypeValidation.FULL_NAME,
    message = "Tên người dùng không đúng định dạng"
  )
  private String fullName;
}
