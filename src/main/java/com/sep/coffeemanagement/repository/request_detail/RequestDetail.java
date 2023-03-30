package com.sep.coffeemanagement.repository.request_detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetail {
  private String requestDetailId;
  private String requestId;
  private String goodsId;
  private double quantity;
  private double applyPrice;
}
