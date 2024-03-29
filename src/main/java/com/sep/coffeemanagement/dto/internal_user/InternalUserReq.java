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
  @NotNull(message = "Tên đăng nhập không đúng định dạng")
  @Pattern(
    regexp = TypeValidation.USERNAME,
    message = "Tên đăng nhập không đúng định dạng"
  )
  private String loginName;

  @NotNull(message = "Số điện thoại không đúng định dạng")
  @Pattern(regexp = TypeValidation.PHONE, message = "Số điện thoại không đúng định dạng")
  private String phoneNumber;

  @NotNull(message = "Email không đúng định dạng")
  @Pattern(regexp = TypeValidation.EMAIL, message = "Email không đúng định dạng")
  private String email;

  @NotEmpty(message = "Tên người dùng không được để trống")
  @NotNull(message = "Tên người dùng không được để trống")
  @NotBlank(message = "Tên người dùng không được để trống")
  private String fullName;
}
