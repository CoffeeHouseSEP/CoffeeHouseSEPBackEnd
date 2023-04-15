package com.sep.coffeemanagement.repository.admin_dashboard;

import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueReq;
import com.sep.coffeemanagement.dto.admin_dashboard_revenue.AdminDashboardRevenueRes;
import com.sep.coffeemanagement.repository.abstract_repository.AbstractRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDashboardRepository extends AbstractRepository {

  public List<AdminDashboardRevenueRes> getListBranchRevenue(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" SELECT ");
    sb.append(" branch_id branchId, ");
    sb.append(" (select name from branch where branch_id = t1.branch_id) branchName, ");
    sb.append(" sum(t1.total_price) revenue, ");
    sb.append(" count(*) totalOrders ");
    sb.append(" FROM ");
    sb.append(" orders t1 ");
    sb.append(
      convertParamsFilterSelectQuery(
        allParams,
        AdminDashboardRevenueReq.class,
        0,
        0,
        keySort,
        sortField,
        ""
      )
    );
    sb.append(" GROUP BY ");
    sb.append(" branch_id ");
    if (pageSize != 0) {
      sb
        .append(" LIMIT ")
        .append(pageSize)
        .append(" OFFSET ")
        .append((page - 1) * pageSize);
    }
    return replaceQuery(sb.toString(), AdminDashboardRevenueRes.class).get();
  }
}
