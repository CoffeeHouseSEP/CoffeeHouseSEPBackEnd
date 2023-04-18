package com.sep.coffeemanagement.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRes {
  private String ordersId;
  private String customerId;
  private String customerName;
  private String branchId;
  private String branchName;
  private String createdDate;
  private double totalPrice;
  private String shippedDate;
  private String address;
  private String province;
  private String ward;
  private String district;
  private String couponId;
  private String couponCode;
  private String status;
  private String approvedDate;
  private String cancelledDate;
  private String reason;
}
