package com.sep.coffeemanagement.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRes {
  private int goodsId;
  private String name;
  private String code;
  private double applyPrice;
  private double innerPrice;
  private String description;
  private int status;
  private int categoryId;
  private String base64String;
  private String categoryName;
}
