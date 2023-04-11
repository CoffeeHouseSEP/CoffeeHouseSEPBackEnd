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
  @GetMapping(value = "get-list-order-detail-by-order-id")
  public ResponseEntity<CommonResponse<ListWrapperResponse<OrderDetailRes>>> getListOrderDetailByOrdersId(
    @RequestParam String orderId,
    HttpServletRequest request
  ) {
    return response(service.getListOrderDetailByOrdersId(orderId), "success");
  }
}
