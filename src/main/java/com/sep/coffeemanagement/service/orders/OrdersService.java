package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
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

  String createOrders(OrdersRes req);
}
