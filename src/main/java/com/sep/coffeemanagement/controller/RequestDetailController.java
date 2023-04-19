package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request_detail.RequestDetailRes;
import com.sep.coffeemanagement.service.request_detail.RequestDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "request-detail")
public class RequestDetailController extends AbstractController<RequestDetailService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-request-detail-by-request-id")
  public ResponseEntity<CommonResponse<ListWrapperResponse<RequestDetailRes>>> getListRequestDetailByRequestId(
    @RequestParam(defaultValue = "0") String requestId,
    HttpServletRequest request
  ) {
    return response(service.getListRequestDetailByRequestId(requestId), "Thành công");
  }
}
