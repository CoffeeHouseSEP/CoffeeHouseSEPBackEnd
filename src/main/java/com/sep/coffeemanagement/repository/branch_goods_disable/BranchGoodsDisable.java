package com.sep.coffeemanagement.repository.branch_goods_disable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGoodsDisable {
  private int id;
  private String branchId;
  private String goodsId;
}
