package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl
  extends AbstractService<OrdersRepository>
  implements OrdersService {
  @Autowired
  private BranchRepository branchRepository;

  @Autowired
  private CouponRepository couponRepository;

  //    @Autowired
  //    private CustomerRepository customerRepository;
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
                orders.getCouponId(),
                orders.getCouponCode(),
                orders.getStatus()
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
  public String createOrders(OrdersReq req) {
    checkValidOrdersRequest(req);
    Orders orders = objectMapper.convertValue(req, Orders.class);
    String newId = UUID.randomUUID().toString();
    orders.setOrdersId(newId);
    orders.setStatus(0);
    repository.insertAndUpdate(orders, false);
    return newId;
  }

  @Override
  public void checkoutOrders(OrdersReq req) {
    checkValidOrdersRequest(req);
    if (req.getShippedDate() == null) {
      throw new InvalidRequestException(new HashMap<>(), "shipped date null");
    }
    if (req.getShippedDate().compareTo(new Date()) <= 0) {
      throw new InvalidRequestException(
        new HashMap<>(),
        "shipped date date must after present"
      );
    }
    Orders orders = repository
      .getOneByAttribute("ordersId", req.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("orders not found"));
    if (StringUtils.isNotEmpty(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(() -> new ResourceNotFoundException("coupon not found"));
      orders.setCouponId(req.getCouponId());
      switch (coupon.getType()) {
        case 1:
          orders.setTotalPrice(
            orders.getTotalPrice() - coupon.getValue() >= 0
              ? orders.getTotalPrice() - coupon.getValue()
              : 0
          );
          break;
        case 2:
          orders.setTotalPrice(orders.getTotalPrice() * coupon.getValue() / 100);
          break;
      }
    }
    orders.setBranchId(req.getBranchId());
    orders.setCreatedDate(DateFormat.getCurrentTime());
    orders.setShippedDate(req.getShippedDate());
    orders.setAddress(req.getAddress());
    orders.setStatus(1); //checkouted
    repository.insertAndUpdate(orders, true);
  }

  public void checkValidOrdersRequest(OrdersReq req) {
    validate(req);
    if (StringUtils.isNotEmpty(req.getBranchId())) {
      Branch branch = branchRepository
        .getOneByAttribute("branchId", req.getBranchId())
        .orElseThrow(() -> new ResourceNotFoundException("branch not found"));
    }
    if (StringUtils.isNotEmpty(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(() -> new ResourceNotFoundException("coupon not found"));
    }
  }
}
