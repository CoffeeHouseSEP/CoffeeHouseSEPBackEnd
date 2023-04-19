package com.sep.coffeemanagement.utils;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import java.util.List;

public class AppParamUtils {

  public static double getRatioValueFromListSizeGoods(
    List<AppParamRes> listSize,
    String size
  ) {
    if (Constant.GOODS_SIZE.S.toString().equals(size)) {
      return 1d;
    } else if (Constant.GOODS_SIZE.M.toString().equals(size)) {
      for (AppParamRes appParam : listSize) {
        if (Constant.GOODS_SIZE.M.toString().equals(appParam.getName())) {
          return Double.parseDouble(appParam.getCode());
        }
      }
      throw new ResourceNotFoundException("app param not found");
    } else if (Constant.GOODS_SIZE.L.toString().equals(size)) {
      for (AppParamRes appParam : listSize) {
        if (Constant.GOODS_SIZE.L.toString().equals(appParam.getName())) {
          return Double.parseDouble(appParam.getCode());
        }
      }
      throw new ResourceNotFoundException("app param not found");
    } else {
      throw new ResourceNotFoundException("Size của sản phẩm không đúng (S, M, L)");
    }
  }
}
