package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.service.order_detail.OrderDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "order-detail")
public class OrderDetailController extends AbstractController<OrderDetailService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-order-detail")
  public ResponseEntity<CommonResponse<ListWrapperResponse<OrderDetailRes>>> getListOrderDetail(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "0") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListOrderDetail(allParams, keySort, page, pageSize, ""),
      "success"
    );
  }
}
