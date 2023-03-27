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

  @NotNull(message = "goods_id null")
  private String goodsId;

  @NotNull(message = "quantity is null")
  @Positive(message = "quantity is negative or zero")
  private double quantity;

  @NotNull(message = "size null")
  private int size;

  private String customerId;
}
