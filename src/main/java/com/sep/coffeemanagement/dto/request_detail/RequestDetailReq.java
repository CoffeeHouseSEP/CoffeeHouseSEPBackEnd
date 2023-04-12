package com.sep.coffeemanagement.dto.request_detail;

import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailReq {
  private String goodsId;

  @Positive(message = "quantity is negative or zero")
  private double quantity;

  private double applyPrice;
}
