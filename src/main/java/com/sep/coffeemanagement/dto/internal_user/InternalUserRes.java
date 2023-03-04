package com.sep.coffeemanagement.dto.internal_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUserRes {
  private int id;
  private String loginName;
  private String phoneNumber;
  private String createdDate;
  private int status;
}
