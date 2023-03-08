package com.sep.coffeemanagement.repository.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
  private int goodsId;
  private String name;
  private String code;
  private double applyPrice;
  //  private double innerPrice;
  private String description;
  private int status;
  private int categoryId;
}
