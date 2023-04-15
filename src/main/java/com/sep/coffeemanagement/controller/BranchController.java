package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.branch.BranchReq;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.service.branch.BranchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "branch")
public class BranchController extends AbstractController<BranchService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-branch")
  public ResponseEntity<CommonResponse<String>> addNewBranch(
    @RequestBody BranchReq branchRequest,
    HttpServletRequest request
  ) {
    service.createBranch(branchRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "create branch success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-branch")
  public ResponseEntity<CommonResponse<String>> updateBranch(
    @RequestBody BranchReq branchRequest,
    HttpServletRequest request
  ) {
    service.updateBranch(branchRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update branch success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-branch")
  public ResponseEntity<CommonResponse<ListWrapperResponse<BranchRes>>> getListBranch(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String role = getUserRoleByRequest(request);
    if (!Constant.ADMIN_ROLE.equals(role)) {
      allParams.put("status", "1");
    }
    return response(
      service.getListBranch(allParams, keySort, page, pageSize, sortField),
      "success"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-branch-by-manager-id")
  public ResponseEntity<CommonResponse<BranchRes>> getBranchByManagerId(
    @RequestParam String branchManagerId
  ) {
    return response(service.getBranchByManagerId(branchManagerId), "success");
  }
}
