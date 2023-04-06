package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableReq;
import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.service.branch_goods_disable.BranchGoodsDisableService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "branch-goods-disable")
public class BranchGoodsDisableController
  extends AbstractController<BranchGoodsDisableService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "disable-goods-from-branch")
  public ResponseEntity<CommonResponse<String>> disableGoodsFromBranch(
    @RequestParam String goodsId,
    HttpServletRequest request
  ) {
    BranchGoodsDisableReq req = new BranchGoodsDisableReq();
    req.setGoodsId(goodsId);
    req.setUserId("7c642b55-ca9c-45db-bc78-287079f2c1a8");
    service.createBranchGoodsDisable(req);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "disable goods success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "enable-goods-from-branch")
  public ResponseEntity<CommonResponse<String>> enableGoodsFromBranch(
    @RequestParam String goodsId,
    HttpServletRequest request
  ) {
    BranchGoodsDisableReq req = new BranchGoodsDisableReq();
    req.setGoodsId(goodsId);
    req.setUserId("7c642b55-ca9c-45db-bc78-287079f2c1a8");
    service.removeBranchGoodsDisable(req);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "enable goods success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-goods-disable")
  public ResponseEntity<CommonResponse<ListWrapperResponse<BranchGoodsDisableRes>>> getListGoodsDisableByBranchId(
    @RequestParam String branchId,
    HttpServletRequest request
  ) {
    return response(service.getListGoodsIdDisableByBranch(branchId), "success");
  }
}
