package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.orders.OrdersReq;
import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.app_param.AppParamRepository;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.branch_goods_disable.BranchGoodsDisable;
import com.sep.coffeemanagement.repository.branch_goods_disable.BranchGoodsDisableRepository;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.order_detail.OrderDetail;
import com.sep.coffeemanagement.repository.order_detail.OrderDetailRepository;
import com.sep.coffeemanagement.repository.orders.Orders;
import com.sep.coffeemanagement.repository.orders.OrdersRepository;
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

  @Autowired
  private BranchGoodsDisableRepository branchGoodsDisableRepository;

  @Autowired
  private AppParamRepository appParamRepository;

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
  public void createOrders(OrdersReq req) {
    //Step 0: generate orders_id and get list app param for goods size:
    String ordersId = UUID.randomUUID().toString();
    List<AppParamRes> listGoodsSize = appParamRepository.getListAppParamByParType(
      "UPSIZE_GOODS"
    );
    //Step 0: END
    //Step 1: check valid order and branch_id:
    validate(req);
    Branch branch = branchRepository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(
        () -> new InvalidRequestException(new HashMap<>(), "branch not found")
      );
    //Step 1: END
    //Step 2: check valid, save order_detail, get total_price
    if (req.getListOrderDetail() == null || req.getListOrderDetail().isEmpty()) {
      throw new InvalidRequestException(new HashMap<>(), "orders no content");
    }
    double orderTotalPrice = 0;
    for (OrderDetailReq orderDetailReq : req.getListOrderDetail()) {
      //Step 2.1: validate
      validate(orderDetailReq);
      //Step 2.2: check if branch disabled this goods
      Goods goods = checkIsGoodsDisabledByBranch(
        orderDetailReq.getGoodsId(),
        branch.getBranchId()
      );
      //Step 2.3: get size
      if (1 == goods.getIsSize()) {
        orderTotalPrice +=
          goods.getApplyPrice() *
          orderDetailReq.getQuantity() *
          getRatioValueFromListSizeGoods(listGoodsSize, orderDetailReq.getSize());
      } else {
        orderTotalPrice += goods.getApplyPrice() * orderDetailReq.getQuantity();
      }
      //Step 2.4: save order_detail
      String orderDetailId = UUID.randomUUID().toString();
      orderDetailReq.setOrderDetailId(orderDetailId);
      orderDetailReq.setOrdersId(ordersId);
      orderDetailReq.setApplyPrice(
        goods.getApplyPrice() *
        getRatioValueFromListSizeGoods(listGoodsSize, orderDetailReq.getSize())
      );
      orderDetailRepository.insertAndUpdate(
        objectMapper.convertValue(orderDetailReq, OrderDetail.class),
        false
      );
    }
    //Step 2: END
    //Step 3: Check valid coupon
    if (StringUtils.isNoneEmpty(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(
          () -> new InvalidRequestException(new HashMap<>(), "coupon not found")
        );
      List<CouponRes> listValidCoupon = couponRepository.getListCouponByCartTotalPrice(
        orderTotalPrice,
        coupon.getCouponId()
      );
      if (listValidCoupon.isEmpty()) {
        throw new InvalidRequestException(new HashMap<>(), "coupon not valid");
      }
    }
    //Step 3: END
    //Step 4: save orders
    Orders ordersSave = new Orders();
    ordersSave.setOrdersId(ordersId);
    ordersSave.setCustomerId(req.getCustomerId());
    ordersSave.setBranchId(req.getBranchId());
    ordersSave.setTotalPrice(orderTotalPrice);
    ordersSave.setShippedDate(req.getShippedDate());
    ordersSave.setCouponId(req.getCouponId());
    ordersSave.setCreatedDate(DateFormat.getCurrentTime());
    ordersSave.setStatus(Constant.ORDER_STATUS.PENDING_APPROVED.toString());
    ordersSave.setAddress(req.getAddress());
    ordersSave.setProvince(req.getProvince());
    ordersSave.setWard(req.getWard());
    ordersSave.setDistrict(req.getDistrict());
    repository.insertAndUpdate(ordersSave, false);
    //Step 4: END
  }

  @Override
  public void changeStatusOrders(OrdersReq req, Constant.ORDER_STATUS status) {
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
        orders.setReason(req.getReason());
      } else {
        throw new InvalidRequestException(
          new HashMap<>(),
          "cancel reason is null or empty"
        );
      }
    }
    repository.insertAndUpdate(orders, true);
  }

  public Goods checkIsGoodsDisabledByBranch(String goodsId, String branchId) {
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", goodsId)
      .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
    if (1 != goods.getIsSold()) {
      throw new InvalidRequestException(
        new HashMap<>(),
        goods.getName() + " is not for sold"
      );
    }
    HashMap<String, String> allParams = new HashMap<>();
    allParams.put("branchId", branchId);
    allParams.put("goodsId", goodsId);
    List<BranchGoodsDisable> list = branchGoodsDisableRepository
      .getListOrEntity(allParams, "asc", 0, 0, "")
      .get();
    if (!list.isEmpty()) {
      throw new InvalidRequestException(
        new HashMap<>(),
        goods.getName() + " is disable from branch"
      );
    }
    return goods;
  }

  public double getRatioValueFromListSizeGoods(List<AppParamRes> listSize, String size) {
    switch (size) {
      case "S":
        return 1d;
      case "M":
        for (AppParamRes appParam : listSize) {
          if ("M".equals(appParam.getName())) {
            return Double.parseDouble(appParam.getCode());
          }
        }
        throw new InvalidRequestException(new HashMap<>(), "something wrong!");
      case "L":
        for (AppParamRes appParam : listSize) {
          if ("L".equals(appParam.getName())) {
            return Double.parseDouble(appParam.getCode());
          }
        }
        throw new InvalidRequestException(new HashMap<>(), "something wrong!");
      default:
        throw new InvalidRequestException(
          new HashMap<>(),
          "wrong size input (only S,M,L)"
        );
    }
  }
}
