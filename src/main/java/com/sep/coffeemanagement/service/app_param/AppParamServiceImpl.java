package com.sep.coffeemanagement.service.app_param;

import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.repository.app_param.AppParamRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AppParamServiceImpl
  extends AbstractService<AppParamRepository>
  implements AppParamService {

  @Override
  public Optional<ListWrapperResponse<AppParamRes>> getListAppParamByParType(
    String parType
  ) {
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
