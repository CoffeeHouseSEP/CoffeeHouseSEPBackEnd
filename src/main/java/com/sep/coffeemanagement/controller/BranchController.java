package com.sep.coffeemanagement.controller;

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
    try {
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
    } catch (InvalidRequestException e) {
      return new ResponseEntity<CommonResponse<String>>(
        new CommonResponse<String>(
          true,
          null,
          "create branch fail: " + e.getMessage(),
          HttpStatus.OK.value()
        ),
        null,
        HttpStatus.OK.value()
      );
    }
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-branch")
  public ResponseEntity<CommonResponse<String>> updateBranch(
    @RequestBody BranchReq branchRequest,
    HttpServletRequest request
  ) {
    try {
      service.updateBranch(branchRequest);
      return new ResponseEntity<CommonResponse<String>>(
        new CommonResponse<String>(
          true,
          null,
          "update news success",
          HttpStatus.OK.value()
        ),
        null,
        HttpStatus.OK.value()
      );
    } catch (InvalidRequestException e) {
      return new ResponseEntity<CommonResponse<String>>(
        new CommonResponse<String>(
          true,
          null,
          "update branch fail: " + e.getMessage(),
          HttpStatus.OK.value()
        ),
        null,
        HttpStatus.OK.value()
      );
    }
  }

  @GetMapping(value = "get-list-branch")
  public ResponseEntity<CommonResponse<ListWrapperResponse<BranchRes>>> getListBranch(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "0") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListBranch(allParams, keySort, page, pageSize, ""),
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
