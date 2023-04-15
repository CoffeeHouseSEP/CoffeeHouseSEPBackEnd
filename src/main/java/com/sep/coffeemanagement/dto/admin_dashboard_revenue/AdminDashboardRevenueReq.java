package com.sep.coffeemanagement.dto.admin_dashboard_revenue;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardRevenueReq {
  private String branchId;
  private Date createdDate;
  private String status;
}
