package com.sep.coffeemanagement.dto.internal_user_profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalUserProfileRes {
  private String loginName;
  private String address;
  private String phoneNumber;
  private String email;
}

