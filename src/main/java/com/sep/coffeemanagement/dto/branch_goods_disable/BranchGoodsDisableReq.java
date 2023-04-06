package com.sep.coffeemanagement.dto.branch_goods_disable;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGoodsDisableReq {
  @NotNull(message = "branch id null")
  private String branchId;

  @NotNull(message = "goods id null")
  private String goodsId;

  private String userId;
}
