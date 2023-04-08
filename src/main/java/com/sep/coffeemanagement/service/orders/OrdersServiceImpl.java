package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.order_detail.OrderDetail;
import com.sep.coffeemanagement.repository.order_detail.OrderDetailRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
import com.sep.coffeemanagement.repository.request.Request;
import com.sep.coffeemanagement.repository.request_detail.RequestDetail;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersServiceImpl
  extends AbstractService<OrdersRepository>
  implements OrdersService {
  @Autowired
  private BranchRepository branchRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private GoodsRepository goodsRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Override
  public Optional<ListWrapperResponse<OrdersRes>> getListOrders(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<OrdersRes> list = repository.getListOrders(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            orders ->
              new OrdersRes(
                orders.getOrdersId(),
                orders.getCustomerId(),
                orders.getCustomerName(),
                orders.getBranchId(),
                orders.getBranchName(),
                orders.getCreatedDate(),
                orders.getTotalPrice(),
                orders.getShippedDate(),
                orders.getAddress(),
                orders.getProvince(),
                orders.getWard(),
                orders.getDistrict(),
                orders.getCouponId(),
                orders.getCouponCode(),
                orders.getStatus(),
                orders.getApprovedDate(),
                orders.getCancelledDate(),
                orders.getReason()
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
  @Transactional
  public void insertOrUpdateOrders(OrdersReq req) {
    String newId = UUID.randomUUID().toString();
    req.setOrdersId(newId);
    double orderTotalPrice = saveOrderDetailReturnTotalPrice(req);
    req.setTotalPrice(orderTotalPrice);
    checkValidOrdersRequest(req);
    if (req.getListOrderDetail() == null || req.getListOrderDetail().isEmpty()) {
      throw new InvalidRequestException(new HashMap<>(), "orders no content");
    }
    Orders ordersSave = new Orders();
    ordersSave.setOrdersId(newId);
    ordersSave.setCustomerId(req.getCustomerId());
    ordersSave.setStatus(Constant.ORDER_STATUS.PENDING_APPROVED.toString());
    ordersSave.setCreatedDate(DateFormat.getCurrentTime());
    ordersSave.setTotalPrice(orderTotalPrice);
    ordersSave.setCouponId(req.getCouponId());
    ordersSave.setBranchId(req.getBranchId());
    ordersSave.setAddress(req.getAddress());
    ordersSave.setProvince(req.getProvince());
    ordersSave.setWard(req.getWard());
    ordersSave.setDistrict(req.getDistrict());
    repository.insertAndUpdate(ordersSave, false);
  }

  @Override
  public void changeStatusOrders(OrdersReq req, Constant.ORDER_STATUS status) {
    validate(req);
    Orders orders = repository
      .getOneByAttribute("ordersId", req.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    if (Constant.ORDER_STATUS.APPROVED == status) {
      if (!Constant.ORDER_STATUS.PENDING_APPROVED.toString().equals(orders.getStatus())) {
        throw new InvalidRequestException(
          new HashMap<>(),
          "orders not in status PENDING_APPROVED"
        );
      }
      orders.setStatus(Constant.ORDER_STATUS.APPROVED.toString());
      orders.setApprovedDate(DateFormat.getCurrentTime());
    } else if (Constant.ORDER_STATUS.CANCELLED == status) {
      if (StringUtils.isNoneEmpty(req.getReason())) {
        orders.setStatus(Constant.REQUEST_STATUS.CANCELLED.toString());
        orders.setCancelledDate(DateFormat.getCurrentTime());
      } else {
        throw new InvalidRequestException(
          new HashMap<>(),
          "cancel reason is null or empty"
        );
      }
    }
    repository.insertAndUpdate(orders, true);
  }

  private double saveOrderDetailReturnTotalPrice(OrdersReq req) {
    double orderTotalPrice = 0;
    for (OrderDetailReq orderDetailReq : req.getListOrderDetail()) {
      validate(orderDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", orderDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
      orderTotalPrice += goods.getApplyPrice() * orderDetailReq.getQuantity();
      String orderDetailId = UUID.randomUUID().toString();
      orderDetailReq.setOrderDetailId(orderDetailId);
      orderDetailReq.setOrdersId(req.getOrdersId());
      orderDetailReq.setApplyPrice(goods.getApplyPrice());
      orderDetailRepository.insertAndUpdate(
        objectMapper.convertValue(orderDetailReq, OrderDetail.class),
        false
      );
    }
    return orderTotalPrice;
  }

  public void checkValidOrdersRequest(OrdersReq req) {
    validate(req);
    Branch branch = branchRepository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(
        () -> new InvalidRequestException(new HashMap<>(), "branch not found")
      );
    if (StringUtils.isNoneEmpty(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(
          () -> new InvalidRequestException(new HashMap<>(), "coupon not found")
        );
      List<CouponRes> listValidCoupon = couponRepository.getListCouponByCartTotalPrice(
        req.getTotalPrice(),
        coupon.getCouponId()
      );
      if (listValidCoupon.isEmpty()) {
        throw new InvalidRequestException(new HashMap<>(), "coupon not valid");
      }
    }
  }
}
