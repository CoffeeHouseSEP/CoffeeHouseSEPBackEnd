package com.sep.coffeemanagement.dto.branch;

import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchReq {
  private String branchId;

  @NotEmpty(message = "Tên không được để trống")
  @NotBlank(message = "Tên không được để trống")
  @NotNull(message = "Tên không được để trống")
  @Length(max = 200, message = "Tên không được vượt quá 200 ký tự")
  private String name;

  @NotEmpty(message = "Địa chỉ không được để trống")
  @NotBlank(message = "Địa chỉ không được để trống")
  @NotNull(message = "Địa chỉ không được để trống")
  @Length(max = 1000, message = "Địa chỉ không dược vượt quá 1000 ký tự")
  private String address;

  @NotNull(message = "Sai định dạng số điện thoại")
  @Pattern(regexp = TypeValidation.PHONE, message = "Sai định dạng số điện thoại")
  private String phoneNumber;

  @NotEmpty(message = "Thông tin không được để trống")
  @NotBlank(message = "Thông tin không được để trống")
  @NotNull(message = "Thông tin không được để trống")
  @Length(max = 8000, message = "Thông tin không được vượt quá 8000 ký tự")
  private String description;

  @NotNull(message = "Quản lý không được để trống")
  private String branchManagerId;

  private int status;

  @NotEmpty(message = "Phường/Xã không được để trống")
  @NotBlank(message = "Phường/Xã không được để trống")
  @NotNull(message = "Phường/Xã không được để trống")
  private String ward;

  @NotEmpty(message = "Quận/Huyện không được để trống")
  @NotBlank(message = "Quận/Huyện không được để trống")
  @NotNull(message = "Quận/Huyện không được để trống")
  private String district;

  @NotEmpty(message = "Tỉnh/Thành phố không được để trống")
  @NotBlank(message = "Tỉnh/Thành phố không được để trống")
  @NotNull(message = "Tỉnh/Thành phố không được để trống")
  private String province;

  private ImageInfoReq image;
}
