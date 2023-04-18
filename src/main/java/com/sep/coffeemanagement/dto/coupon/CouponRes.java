package com.sep.coffeemanagement.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRes {
  private String couponId;
  private String code;
  private double value;
  private int status;
  private String createdDate;
  private String expiredDate;
  private String appliedDate;
  private double maxValuePromotion;
}
