package com.sep.coffeemanagement.repository.order_detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
  private String orderDetailId;
  private String ordersId;
  private String goodsId;
  private double quantity;
  private int size;
}
