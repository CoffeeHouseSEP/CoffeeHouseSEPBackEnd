package com.sep.coffeemanagement.repository.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
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
}
