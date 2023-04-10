package com.sep.coffeemanagement.dto.branch;

import java.util.Date;
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
  private String longitude;
  private String latitude;
  private int status;
  private Date createdDate;
  private Date cancelledDate;
  private String ward;
  private String district;
  private String province;
  private String street;
  private String branchManagerName;
}
