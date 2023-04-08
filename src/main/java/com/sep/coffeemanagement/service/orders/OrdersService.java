package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import java.util.Map;
import java.util.Optional;

public interface OrdersService {
  Optional<ListWrapperResponse<OrdersRes>> getListOrders(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createOrders(OrdersReq req);

  void changeStatusOrders(OrdersReq req, Constant.ORDER_STATUS status);
}
