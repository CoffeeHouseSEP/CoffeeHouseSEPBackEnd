package com.sep.coffeemanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRes {
  private String requestId;
  private String branchId;
  private String createdBy;
  private String createdDate;
  private String status;
  private String approvedBy;
  private String approvedDate;
  private String completedDate;
  private String cancelledDate;
  private String reason;
  private double totalPrice;
  private String branchName;
  private String createdByName;
  private String approvedByName;
}
