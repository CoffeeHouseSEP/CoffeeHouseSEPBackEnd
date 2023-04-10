package com.sep.coffeemanagement.service.branch_goods_disable;

import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableReq;
import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import java.util.Optional;

public interface BranchGoodsDisableService {
  Optional<ListWrapperResponse<BranchGoodsDisableRes>> getListGoodsIdDisableByBranch(
    String branchId
  );

  void createBranchGoodsDisable(BranchGoodsDisableReq req);
  void removeBranchGoodsDisable(BranchGoodsDisableReq req);
}
