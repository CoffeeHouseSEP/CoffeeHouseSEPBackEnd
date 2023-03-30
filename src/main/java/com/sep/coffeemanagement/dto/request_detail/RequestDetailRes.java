package com.sep.coffeemanagement.dto.request_detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailRes {
  private String requestDetailId;
  private String requestId;
  private String goodsId;
  private double quantity;
  private double applyPrice;
  private String goodsName;
}
