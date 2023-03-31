package com.sep.coffeemanagement.service.order_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import java.util.Optional;

public interface OrderDetailService {
  Optional<ListWrapperResponse<OrderDetailRes>> getListOrderDetailByOrdersId(
    String ordersId
  );
}
