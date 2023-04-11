package com.sep.coffeemanagement.dto.coupon;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRes {
  private String couponId;
  private String code;
  private int type;
  private double value;
  private int status;
  private Date createdDate;
  private Date expiredDate;
  private Date appliedDate;
  private double maxValuePromotion;
}
