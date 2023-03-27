package com.sep.coffeemanagement.dto.app_param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppParamRes {
  private String code;
  private String name;
  private String parType;
  private int parOrder;
  private int status;
}
