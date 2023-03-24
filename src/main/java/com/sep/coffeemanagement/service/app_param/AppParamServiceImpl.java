package com.sep.coffeemanagement.service.app_param;

import com.sep.coffeemanagement.dto.app_param.AppParamReq;
import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.repository.app_param.AppParamRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AppParamServiceImpl
  extends AbstractService<AppParamRepository>
  implements AppParamService {

  @Override
  public Optional<ListWrapperResponse<AppParamRes>> getListAppParamByParType(
    String parType
  ) {
    if (!StringUtils.isNoneEmpty(parType)) {
      throw new InvalidRequestException(new HashMap<>(), "par_type is null");
    }
    List<AppParamRes> list = repository.getListAppParamByParType(parType);
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            appParam ->
              new AppParamRes(
                appParam.getCode(),
                appParam.getName(),
                appParam.getParType(),
                appParam.getParOrder(),
                appParam.getStatus()
              )
          )
          .collect(Collectors.toList()),
        0,
        0,
        list.size()
      )
    );
  }
}
