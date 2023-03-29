package com.sep.coffeemanagement.dto.request;

import java.util.Date;
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
  private Date createdDate;
  private String status;
  private String approvedBy;
  private Date approvedDate;
  private Date completedDate;
  private Date cancelledDate;
  private String reason;
  private double totalPrice;
  private String branchName;
  private String createdByName;
  private String approvedByName;
}
