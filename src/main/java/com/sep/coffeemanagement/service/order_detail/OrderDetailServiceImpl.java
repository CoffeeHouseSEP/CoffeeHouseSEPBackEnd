package com.sep.coffeemanagement.service.order_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.repository.order_detail.OrderDetailRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl
  extends AbstractService<OrderDetailRepository>
  implements OrderDetailService {

  @Override
  public Optional<ListWrapperResponse<OrderDetailRes>> getListOrderDetailByOrdersId(
    String ordersId
  ) {
    List<OrderDetailRes> list = repository.getListOrderDetailByOrdersId(ordersId);
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            orderDetail ->
              new OrderDetailRes(
                orderDetail.getOrderDetailId(),
                orderDetail.getOrdersId(),
                orderDetail.getGoodsId(),
                orderDetail.getQuantity(),
                orderDetail.getSize(),
                orderDetail.getApplyPrice(),
                orderDetail.getGoodsName()
              )
          )
          .collect(Collectors.toList()),
        0,
        0,
        list.size()
      )
    );
  }
}
