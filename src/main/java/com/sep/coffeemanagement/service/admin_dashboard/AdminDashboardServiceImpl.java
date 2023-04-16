package com.sep.coffeemanagement.service.admin_dashboard;

import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueRes;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.repository.admin_dashboard.AdminDashboardRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardServiceImpl
  extends AbstractService<AdminDashboardRepository>
  implements AdminDashboardService {

  @Override
  public Optional<ListWrapperResponse<AdminDashboardRevenueRes>> getListBranchRevenue(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<AdminDashboardRevenueRes> list = repository.getListBranchRevenue(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            revenue ->
              new AdminDashboardRevenueRes(
                revenue.getBranchId(),
                revenue.getBranchName(),
                revenue.getRevenue(),
                revenue.getTotalOrders()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        list.size()
      )
    );
  }
}
