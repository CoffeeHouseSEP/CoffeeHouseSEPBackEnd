package com.sep.coffeemanagement.repository.orders;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
  private String ordersId;
  private String customerId;
  private String branchId;
  private Date createdDate;
  private double totalPrice;
  private Date shippedDate;
  private String address;
  private String couponId;
  private String status;
  private Date approvedDate;
  private Date cancelledDate;
  private String reason;
  private String province;
  private String ward;
  private String district;
  private String description;
  private String phoneNumber;
}
