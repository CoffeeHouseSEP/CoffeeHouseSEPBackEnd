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
  private String goodsId;

  @Positive(message = "Số lượng đặt hàng phải lớn hơn 0")
  private double quantity;

  private String size;
}
