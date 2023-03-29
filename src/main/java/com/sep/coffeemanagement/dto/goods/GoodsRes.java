package com.sep.coffeemanagement.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRes {
  private String goodsId;
  private String name;
  private String code;
  private double applyPrice;
  private double innerPrice;
  private String description;
  private int status;
  private String categoryId;
  private int isSize;
  private int isSold;
  private String goodsUnit;
  private String categoryName;
}
