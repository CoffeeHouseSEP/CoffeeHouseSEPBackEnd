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

  @NotNull(message = "coupon code is null")
  @Length(max = 50, message = "coupon code over length(50)")
  private String code;

  private int type;

  @Max(value = 70, message = "coupon value not in [0,70]")
  @Min(value = 0, message = "coupon value not in [0,70]")
  private double value;

  private int status;
  private Date createdDate;

  @NotNull(message = "coupon expired date is null")
  private Date expiredDate;

  @NotNull(message = "coupon applied date is null")
  private Date appliedDate;

  @Positive(message = "coupon max value promotion is negative or zero")
  private double maxValuePromotion;
}
