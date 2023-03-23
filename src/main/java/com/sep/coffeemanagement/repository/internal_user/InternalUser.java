package com.sep.coffeemanagement.repository.internal_user;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUser {
  private String internalUserId;
  private String loginName;
  private String encrPassword;
  private String email;
  private String phoneNumber;
  private Date createdDate;
  private int status;
  private String token;
  private String address;
  private String role;
}
