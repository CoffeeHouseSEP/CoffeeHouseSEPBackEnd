package com.sep.coffeemanagement.service.order_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import java.util.Map;
import java.util.Optional;

public interface OrderDetailService {
  Optional<ListWrapperResponse<OrderDetailRes>> getListOrderDetail(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createOrderDetail(OrderDetailReq req);

  void updateOrderDetail(OrderDetailReq req);

  void removeOrderDetail(String orderDetailId);
}
