package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.service.app_param.AppParamService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "app-param")
public class AppParamController extends AbstractController<AppParamService> {

  @GetMapping(value = "get-list-app-param-by-par-type")
  public ResponseEntity<CommonResponse<ListWrapperResponse<AppParamRes>>> getListAppParamByParType(
    @RequestParam(defaultValue = "") String parType,
    HttpServletRequest request
  ) {
    return response(service.getListAppParamByParType(parType), "success");
  }
}
