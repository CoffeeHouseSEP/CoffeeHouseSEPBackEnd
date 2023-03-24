package com.sep.coffeemanagement.dto.order_detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRes {
  private String orderDetailId;
  private String ordersId;
  private String goodsId;
  private double quantity;
  private int size;
}
