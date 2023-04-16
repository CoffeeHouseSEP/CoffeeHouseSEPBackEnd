package com.sep.coffeemanagement.dto.goods_branch;

import com.sep.coffeemanagement.dto.goods.GoodsReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBranchReq {
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
  private int isTransfer;
  private String goodsUnit;
  private String branchId;
  private int isDisabled;
}
