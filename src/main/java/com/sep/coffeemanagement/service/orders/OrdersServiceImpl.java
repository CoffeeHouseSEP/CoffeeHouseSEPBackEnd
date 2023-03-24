package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
  public String createOrders(OrdersRes req) {
    checkValidOrdersRequest(req);
    Orders orders = objectMapper.convertValue(req, Orders.class);
    String newId = UUID.randomUUID().toString();
    orders.setOrdersId(newId);
    orders.setStatus(0);
    repository.insertAndUpdate(orders, false);
    return newId;
  }

  public void checkValidOrdersRequest(OrdersRes req) {
    validate(req);
    Branch branch = branchRepository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(() -> new ResourceNotFoundException("branch not found"));
    if (StringUtils.isNotEmpty(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(() -> new ResourceNotFoundException("coupon not found"));
    }
  }
}
