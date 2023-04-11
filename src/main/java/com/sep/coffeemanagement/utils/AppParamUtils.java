package com.sep.coffeemanagement.utils;

import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import java.util.List;

public class AppParamUtils {

  public static double getRatioValueFromListSizeGoods(
    List<AppParamRes> listSize,
    String size
  ) {
    switch (size) {
      case "S":
        return 1d;
      case "M":
        for (AppParamRes appParam : listSize) {
          if ("M".equals(appParam.getName())) {
            return Double.parseDouble(appParam.getCode());
          }
        }
        throw new ResourceNotFoundException("app param not found");
      case "L":
        for (AppParamRes appParam : listSize) {
          if ("L".equals(appParam.getName())) {
            return Double.parseDouble(appParam.getCode());
          }
        }
        throw new ResourceNotFoundException("app param not found");
      default:
        throw new ResourceNotFoundException("wrong size input (only S,M,L)");
    }
  }
}
