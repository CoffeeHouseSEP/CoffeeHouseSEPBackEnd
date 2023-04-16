package com.sep.coffeemanagement.repository.coupon;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
  private String couponId;
  private String code;
  private double value;
  private int status;
  private Date createdDate;
  private Date expiredDate;
  private Date appliedDate;
  private double maxValuePromotion;
}
