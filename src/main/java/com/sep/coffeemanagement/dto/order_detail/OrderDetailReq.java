package com.sep.coffeemanagement.dto.order_detail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailReq {
  private String orderDetailId;
  private String ordersId;
  private String goodsId;

  @Positive(message = "quantity is negative or zero")
  private double quantity;

  private String size;

  private double applyPrice;
}
