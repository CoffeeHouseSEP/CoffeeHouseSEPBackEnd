package com.sep.coffeemanagement.dto.goods_branch;

import com.sep.coffeemanagement.dto.goods.GoodsRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBranchRes extends GoodsRes {
  private int isDisabled;

  public GoodsBranchRes(
    String goodsId,
    String name,
    String code,
    double applyPrice,
    double innerPrice,
    String description,
    int status,
    String categoryId,
    int isSize,
    int isSold,
    int isTransfer,
    String goodsUnit,
    String categoryName,
    int isDisabled
  ) {
    super(
      goodsId,
      name,
      code,
      applyPrice,
      innerPrice,
      description,
      status,
      categoryId,
      isSize,
      isSold,
      isTransfer,
      goodsUnit,
      categoryName
    );
    this.isDisabled = isDisabled;
  }
}
