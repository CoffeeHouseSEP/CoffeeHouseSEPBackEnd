package com.sep.coffeemanagement.repository.app_param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppParam {
  private String code;
  private String name;
  private String parType;
  private int parOrder;
  private int status;
}
