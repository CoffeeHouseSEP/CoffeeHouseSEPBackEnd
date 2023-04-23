package com.sep.coffeemanagement.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchRes {
  private String branchId;
  private String name;
  private String address;
  private String phoneNumber;
  private String description;
  private String branchManagerId;
  private int status;
  private String createdDate;
  private String cancelledDate;
  private String ward;
  private String district;
  private String province;
  private String branchManagerName;
}
