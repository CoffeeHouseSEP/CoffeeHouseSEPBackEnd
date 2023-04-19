package com.sep.coffeemanagement.dto.coupon;

import java.util.Date;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponReq {
  private String couponId;

  @NotNull(message = "Mã không được để trống")
  @NotEmpty(message = "Mã không được để trống")
  @NotBlank(message = "Mã không được để trống")
  @Length(max = 50, message = "Mã không được vượt quá 50 ký tự")
  private String code;

  @Max(value = 70, message = "Giá trị mã giảm giá phải trong khoảng 0 đến 70")
  @Min(value = 0, message = "Giá trị mã giảm giá phải trong khoảng 0 đến 70")
  private double value;

  private int status;
  private Date createdDate;

  @NotNull(message = "Ngày hết hạn không được để trống")
  private Date expiredDate;

  @NotNull(message = "Ngày áp dụng không được để trống")
  private Date appliedDate;

  @Positive(message = "Giá trị giảm giá tối đa phải lớn hơn 0")
  private double maxValuePromotion;
}
