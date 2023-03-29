package com.sep.coffeemanagement.service.app_param;

import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import java.util.Optional;

public interface AppParamService {
  Optional<ListWrapperResponse<AppParamRes>> getListAppParamByParType(String parType);
}
