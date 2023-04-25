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
  @NotNull(message = "Tên đăng nhập không đúng định dạng")
  @Pattern(
    regexp = TypeValidation.USERNAME,
    message = "Tên đăng nhập không đúng định dạng"
  )
  private String registerName;

  @NotNull(message = "Mật khẩu không được để trống")
  private String registerPassword;

  @NotNull(message = "Email không đúng định dạng")
  @Pattern(regexp = TypeValidation.EMAIL, message = "Email không đúng định dạng")
  private String email;

  @NotNull(message = "Số điện thoại không được để trống")
  @Pattern(regexp = TypeValidation.PHONE, message = "Số điện thoại không được để trống")
  private String phone;

  @NotEmpty(message = "Tên người dùng không được để trống")
  @NotNull(message = "Tên người dùng không được để trống")
  @NotBlank(message = "Tên người dùng không được để trống")
  private String fullName;
}
