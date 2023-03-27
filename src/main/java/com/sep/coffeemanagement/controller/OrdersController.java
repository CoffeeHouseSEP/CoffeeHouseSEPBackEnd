package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.service.order_detail.OrderDetailService;
import com.sep.coffeemanagement.service.orders.OrdersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "orders")
public class OrdersController extends AbstractController<OrdersService> {
  @Autowired
  private OrderDetailService orderDetailService;

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
  @PostMapping(value = "add-order-detail")
  public ResponseEntity<CommonResponse<String>> addOrderDetail(
    @RequestBody OrderDetailReq orderDetailReq,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    orderDetailReq.setCustomerId(userId);
    orderDetailService.createOrderDetail(orderDetailReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "save order detail success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-order-detail")
  public ResponseEntity<CommonResponse<String>> updateOrderDetail(
    @RequestBody OrderDetailReq orderDetailReq,
    HttpServletRequest request
  ) {
    orderDetailService.updateOrderDetail(orderDetailReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update order detail success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping(value = "remove-order-detail")
  public ResponseEntity<CommonResponse<String>> removeOrderDetail(
    @RequestParam String orderDetailId,
    HttpServletRequest request
  ) {
    orderDetailService.removeOrderDetail(orderDetailId);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "remove order detail success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "check-out-order")
  public ResponseEntity<CommonResponse<String>> checkoutOrder(
    @RequestBody OrdersReq ordersReq,
    HttpServletRequest request
  ) {
    service.checkoutOrders(ordersReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "checkout success", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }
}
