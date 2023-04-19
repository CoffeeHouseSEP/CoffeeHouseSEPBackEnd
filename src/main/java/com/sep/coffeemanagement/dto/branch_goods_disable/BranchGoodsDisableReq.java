package com.sep.coffeemanagement.dto.branch_goods_disable;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGoodsDisableReq {
  @NotNull(message = "Chi nhánh không được để trống")
  private String branchId;

  @NotNull(message = "Sản phẩm không được để trống")
  private String goodsId;

  private String userId;
}
