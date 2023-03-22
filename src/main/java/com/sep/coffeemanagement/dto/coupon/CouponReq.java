package com.sep.coffeemanagement.dto.coupon;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponReq {
  private String couponId;

  @NotNull(message = "coupon code is null")
  @Length(max = 50, message = "coupon code over length(50)")
  private String code;

  private int type;

  @Positive(message = "coupon value is negative or zero")
  private double value;

  private int status;
  private Date createdDate;

  @NotNull(message = "coupon expired date is null")
  private Date expiredDate;

  @NotNull(message = "coupon applied date is null")
  private Date appliedDate;
}
