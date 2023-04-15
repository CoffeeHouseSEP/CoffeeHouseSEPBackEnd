package com.sep.coffeemanagement.service.admin_dashboard;

import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueReq;
import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import java.util.Map;
import java.util.Optional;

public interface AdminDashboardService {
  Optional<ListWrapperResponse<AdminDashboardRevenueRes>> getListBranchRevenue(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );
}
