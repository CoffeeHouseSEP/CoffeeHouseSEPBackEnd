package com.sep.coffeemanagement.dto.orders;

import java.util.Date;
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
  private Date createdDate;
  private double totalPrice;
  private Date shippedDate;
  private String address;
  private String couponId;
  private String couponCode;
  private String status;
  private Date approvedDate;
  private Date cancelledDate;
  private String reason;
}
