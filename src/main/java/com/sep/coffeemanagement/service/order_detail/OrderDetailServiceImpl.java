package com.sep.coffeemanagement.service.order_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.order_detail.OrderDetail;
import com.sep.coffeemanagement.repository.order_detail.OrderDetailRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.service.orders.OrdersService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl
  extends AbstractService<OrderDetailRepository>
  implements OrderDetailService {
  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private OrdersService ordersService;

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
    //1. start check exist order of customer with status 0 : uncheckout
    String ordersId = "";
    String customerId = req.getCustomerId();
    //**
    OrdersRes order = ordersRepository.getUncheckoutOrderByInternalUserId(customerId);
    //2. if null then create
    if (order == null) {
      //3. get goods by goodsId;
      Goods goods = goodsRepository.getOneByAttribute("goodsId", req.getGoodsId()).get();
      //4. save order
      OrdersReq newOrdersReq = new OrdersReq();
      newOrdersReq.setCustomerId(customerId);
      newOrdersReq.setTotalPrice(req.getQuantity() * goods.getApplyPrice());
      ordersId = ordersService.createOrders(newOrdersReq);
      //5. end save order get order id
      //2. if not null then get order id and update total price
    } else {
      ordersId = order.getOrdersId();
      //3. update total price
      double oldTotalPrice = order.getTotalPrice();
      //4. get goods by goods id
      Goods goods = goodsRepository.getOneByAttribute("goodsId", req.getGoodsId()).get();
      order.setTotalPrice(oldTotalPrice + req.getQuantity() * goods.getApplyPrice());
      ordersRepository.insertAndUpdate(
        objectMapper.convertValue(order, Orders.class),
        true
      );
      //5. end update order
    }
    OrderDetail orderDetail = objectMapper.convertValue(req, OrderDetail.class);
    String newId = UUID.randomUUID().toString();
    orderDetail.setOrderDetailId(newId);
    orderDetail.setOrdersId(ordersId);
    repository.insertAndUpdate(orderDetail, false);
  }

  @Override
  public void updateOrderDetail(OrderDetailReq req) {
    checkValidOrderDetailRequest(req);
    OrderDetail orderDetail = repository
      .getOneByAttribute("orderDetailId", req.getOrderDetailId())
      .orElseThrow(() -> new ResourceNotFoundException("order detail not found"));
    //update total price
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", req.getGoodsId())
      .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
    Orders orders = ordersRepository
      .getOneByAttribute("ordersId", orderDetail.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("orders not found"));
    double newTotalPrice =
      //giá gốc
      orders.getTotalPrice() -
      // trừ giá mua ban đầu
      orderDetail.getQuantity() *
      goods.getApplyPrice() +
      // cộng giá mua mới
      req.getQuantity() *
      goods.getApplyPrice();
    orders.setTotalPrice(newTotalPrice);
    System.out.println(orders.getTotalPrice());
    orderDetail.setGoodsId(req.getGoodsId());
    orderDetail.setQuantity(req.getQuantity());
    orderDetail.setSize(req.getSize());
    repository.insertAndUpdate(orderDetail, true);
    ordersRepository.insertAndUpdate(orders, true);
  }

  @Override
  public void removeOrderDetail(String orderDetailId) {
    OrderDetail orderDetail = repository
      .getOneByAttribute("orderDetailId", orderDetailId)
      .orElseThrow(() -> new ResourceNotFoundException("order detail not found"));
    //update total price
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", orderDetail.getGoodsId())
      .orElseThrow(() -> new ResourceNotFoundException("orders not found"));
    Orders orders = ordersRepository
      .getOneByAttribute("ordersId", orderDetail.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("orders not found"));
    orders.setTotalPrice(
      //giá gốc
      orders.getTotalPrice() -
      // trừ giá mua ban đầu
      orderDetail.getQuantity() *
      goods.getApplyPrice()
    );
    ordersRepository.insertAndUpdate(
      objectMapper.convertValue(orders, Orders.class),
      true
    );
    repository.removeOrderDetail(orderDetailId);
  }

  private void checkValidOrderDetailRequest(OrderDetailReq req) {
    validate(req);
  }
}
