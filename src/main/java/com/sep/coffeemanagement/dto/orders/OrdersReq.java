package com.sep.coffeemanagement.dto.orders;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersReq {
  private String ordersId;

  @NotNull(message = "customer not found")
  private String customerId;

  private String branchId;
  private Date createdDate;
  private double totalPrice;
  private Date shippedDate;
  private String address;
  private String couponId;
  private int status;
}
