package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.service.orders.OrdersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "orders")
public class OrdersController extends AbstractController<OrdersService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-orders")
  public ResponseEntity<CommonResponse<ListWrapperResponse<OrdersRes>>> getListOrders(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListOrders(allParams, keySort, page, pageSize, ""),
      "success"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "create-orders")
  public ResponseEntity<CommonResponse<String>> createOrders(
    @RequestBody OrdersReq ordersReq,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    ordersReq.setCustomerId(userId);
    service.createOrders(ordersReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "create order success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "cancel-orders")
  public ResponseEntity<CommonResponse<String>> cancelOrders(
    @RequestParam String ordersId,
    @RequestParam String cancelReason,
    HttpServletRequest request
  ) {
    OrdersReq ordersRequest = new OrdersReq();
    ordersRequest.setOrdersId(ordersId);
    ordersRequest.setReason(cancelReason);
    service.changeStatusOrders(ordersRequest, Constant.ORDER_STATUS.CANCELLED);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "cancel order success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "approve-orders")
  public ResponseEntity<CommonResponse<String>> approveOrders(
    @RequestParam String ordersId,
    HttpServletRequest request
  ) {
    OrdersReq ordersRequest = new OrdersReq();
    ordersRequest.setOrdersId(ordersId);
    service.changeStatusOrders(ordersRequest, Constant.ORDER_STATUS.APPROVED);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "approve order success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
