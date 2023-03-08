package com.sep.coffeemanagement.dto.internal_user_login;

import com.sep.coffeemanagement.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUserLoginRes {
  private String token;
  private String tokenType = Constant.BEARER;

  public InternalUserLoginRes(String token) {
    this.token = token;
  }
}
