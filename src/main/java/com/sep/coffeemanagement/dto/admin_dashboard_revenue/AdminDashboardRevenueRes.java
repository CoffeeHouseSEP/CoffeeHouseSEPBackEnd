package com.sep.coffeemanagement.dto.admin_dashboard_revenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardRevenueRes {
  private String branchId;
  private String branchName;
  private double revenue;
  private int totalOrders;
}
