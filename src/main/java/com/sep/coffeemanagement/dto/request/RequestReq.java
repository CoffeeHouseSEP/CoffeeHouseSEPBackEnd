package com.sep.coffeemanagement.dto.request;

import com.sep.coffeemanagement.dto.request_detail.RequestDetailReq;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReq {
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
  private List<RequestDetailReq> listRequestDetail;
}
