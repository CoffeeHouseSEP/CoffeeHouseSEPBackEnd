package com.sep.coffeemanagement.repository.branch;

import com.sep.coffeemanagement.constant.TypeValidation;
import java.util.Date;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
  private String branchId;
  private String name;
  private String address;
  private String phoneNumber;
  private String description;
  private String branchManagerId;
  private int status;
  private Date createdDate;
  private Date cancelledDate;
  private String ward;
  private String district;
  private String province;
  private String street;
}
