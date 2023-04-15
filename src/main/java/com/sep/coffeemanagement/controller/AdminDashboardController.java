package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueRes;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.service.admin_dashboard.AdminDashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "admin-dashboard")
public class AdminDashboardController extends AbstractController<AdminDashboardService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-branch-revenue")
  public ResponseEntity<CommonResponse<ListWrapperResponse<AdminDashboardRevenueRes>>> getListBranchRevenue(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    allParams.put("status", Constant.ORDER_STATUS_SUMMARY_REVENUE);
    return response(
      service.getListBranchRevenue(allParams, keySort, page, pageSize, sortField),
      "success"
    );
  }
}
