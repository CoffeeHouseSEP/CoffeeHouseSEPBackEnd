package com.sep.coffeemanagement.service.branch;

import com.sep.coffeemanagement.dto.branch.BranchReq;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import java.util.Map;
import java.util.Optional;

public interface BranchService {
  Optional<BranchRes> getBranchByManagerId(String managerId);

  Optional<ListWrapperResponse<BranchRes>> getListBranch(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createBranch(BranchReq req);

  void updateBranch(BranchReq req);
}
