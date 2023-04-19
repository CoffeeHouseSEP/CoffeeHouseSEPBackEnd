package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.orders.OrdersCreateReq;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.service.branch.BranchService;
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
  private BranchService branchService;

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-orders")
  public ResponseEntity<CommonResponse<ListWrapperResponse<OrdersRes>>> getListOrders(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    String role = getUserRoleByRequest(request);
    if (Constant.BRANCH_ROLE.equals(role)) {
      allParams.put(
        "branchId",
        branchService.getBranchByManagerId(userId).get().getBranchId()
      );
    } else if (Constant.USER_ROLE.equals(role)) {
      allParams.put("customerId", userId);
    } else if (Constant.ADMIN_ROLE.equals(role)) {
      allParams.put("status", Constant.ORDER_STATUS.APPROVED.toString());
    }
    return response(
      service.getListOrders(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "create-orders")
  public ResponseEntity<CommonResponse<String>> createOrders(
    @RequestBody OrdersCreateReq ordersReq,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    ordersReq.setCustomerId(userId);
    service.createOrders(ordersReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Đặt hàng thành công",
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
        "Hủy đơn hàng thành công",
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
        "Duyệt đơn hàng thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
