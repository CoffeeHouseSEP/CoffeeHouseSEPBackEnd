package com.sep.coffeemanagement.service.order_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.order_detail.OrderDetail;
import com.sep.coffeemanagement.repository.order_detail.OrderDetailRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl
  extends AbstractService<OrderDetailRepository>
  implements OrderDetailService {
  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private GoodsRepository goodsRepository;

  @Override
  public Optional<ListWrapperResponse<OrderDetailRes>> getListOrderDetail(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<OrderDetail> list = repository
      .getListOrEntity(allParams, keySort, page, pageSize, sortField)
      .get();
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
                orderDetail.getSize()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createOrderDetail(OrderDetailReq req) {
    checkValidOrderDetailRequest(req);
    OrderDetail orderDetail = objectMapper.convertValue(req, OrderDetail.class);
    String newId = UUID.randomUUID().toString();
    orderDetail.setOrderDetailId(newId);
    repository.insertAndUpdate(orderDetail, false);
  }

  @Override
  public void updateOrderDetail(OrderDetailReq req) {
    checkValidOrderDetailRequest(req);
    OrderDetail orderDetail = repository
      .getOneByAttribute("orderDetailId", req.getOrderDetailId())
      .orElseThrow(() -> new ResourceNotFoundException("order detail not found"));
    orderDetail.setGoodsId(req.getGoodsId());
    orderDetail.setQuantity(req.getQuantity());
    orderDetail.setSize(req.getSize());
    repository.insertAndUpdate(orderDetail, true);
  }

  @Override
  public void removeOrderDetail(String orderDetailId) {
    OrderDetail orderDetail = repository
      .getOneByAttribute("orderDetailId", orderDetailId)
      .orElseThrow(() -> new ResourceNotFoundException("order detail not found"));
    repository.removeOrderDetail(orderDetailId);
  }

  private void checkValidOrderDetailRequest(OrderDetailReq req) {
    validate(req);
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", req.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
    Orders orders = ordersRepository
      .getOneByAttribute("ordersId", req.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("orders not found"));
  }
}
