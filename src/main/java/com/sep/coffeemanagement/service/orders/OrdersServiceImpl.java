package com.sep.coffeemanagement.service.orders;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.dto.orders.OrdersCreateReq;
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
import com.sep.coffeemanagement.utils.AppParamUtils;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
                DateFormat.convertDateStringFormat(
                  orders.getCreatedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                orders.getTotalPrice(),
                DateFormat.convertDateStringFormat(
                  orders.getShippedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                orders.getAddress(),
                orders.getProvince(),
                orders.getWard(),
                orders.getDistrict(),
                orders.getCouponId(),
                orders.getCouponCode(),
                orders.getStatus(),
                DateFormat.convertDateStringFormat(
                  orders.getApprovedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                DateFormat.convertDateStringFormat(
                  orders.getCancelledDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
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
  public void createOrders(OrdersCreateReq req) {
    Map<String, String> errors = generateError(OrdersCreateReq.class);
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
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh"));
    //Step 1: END
    //Step 2: check valid, save order_detail, get total_price
    if (req.getListOrderDetail() == null || req.getListOrderDetail().isEmpty()) {
      errors.put("listOrderDetail", "Giỏ hàng trống");
      throw new InvalidRequestException(errors, "Giỏ hàng trống");
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
          AppParamUtils.getRatioValueFromListSizeGoods(
            listGoodsSize,
            orderDetailReq.getSize()
          );
      } else {
        orderTotalPrice += goods.getApplyPrice() * orderDetailReq.getQuantity();
      }
      //Step 2.4: save order_detail
      String orderDetailId = UUID.randomUUID().toString();
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setOrderDetailId(orderDetailId);
      orderDetail.setOrdersId(ordersId);
      orderDetail.setGoodsId(orderDetailReq.getGoodsId());
      orderDetail.setQuantity(orderDetailReq.getQuantity());
      orderDetail.setSize(orderDetailReq.getSize());
      orderDetail.setApplyPrice(
        goods.getApplyPrice() *
        AppParamUtils.getRatioValueFromListSizeGoods(
          listGoodsSize,
          orderDetailReq.getSize()
        )
      );
      orderDetailRepository.insertAndUpdate(orderDetail, false);
    }
    //Step 2: END
    //Step 3: Check valid coupon
    if (StringUtils.hasText(req.getCouponId())) {
      Coupon coupon = couponRepository
        .getOneByAttribute("couponId", req.getCouponId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá"));
      List<CouponRes> listValidCoupon = couponRepository.getListCouponByCartTotalPrice(
        orderTotalPrice,
        coupon.getCouponId()
      );
      if (listValidCoupon.isEmpty()) {
        errors.put("couponId", "Mã giảm giá không hợp lệ");
        throw new InvalidRequestException(errors, "Mã giảm giá không hợp lệ");
      }
      orderTotalPrice -=
        Math.min(
          orderTotalPrice * coupon.getValue() / 100,
          coupon.getMaxValuePromotion()
        );
    }
    //Step 3: END
    //Step 4: save orders
    Orders ordersSave = new Orders();
    ordersSave.setOrdersId(ordersId);
    ordersSave.setCustomerId(req.getCustomerId());
    ordersSave.setBranchId(req.getBranchId());
    ordersSave.setTotalPrice(orderTotalPrice);
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
    Map<String, String> errors = generateError(OrdersReq.class);
    Orders orders = repository
      .getOneByAttribute("ordersId", req.getOrdersId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
    if (Constant.ORDER_STATUS.APPROVED == status) {
      if (!Constant.ORDER_STATUS.PENDING_APPROVED.toString().equals(orders.getStatus())) {
        errors.put("status", "Đơn hàng không ở trong trạng thái CHỜ DUYỆT");
        throw new InvalidRequestException(
          errors,
          "Đơn hàng không ở trong trạng thái CHỜ DUYỆT"
        );
      }
      orders.setStatus(Constant.ORDER_STATUS.APPROVED.toString());
      orders.setApprovedDate(DateFormat.getCurrentTime());
    } else if (Constant.ORDER_STATUS.CANCELLED == status) {
      if (StringUtils.hasText(req.getReason())) {
        orders.setStatus(Constant.REQUEST_STATUS.CANCELLED.toString());
        orders.setCancelledDate(DateFormat.getCurrentTime());
        orders.setReason(req.getReason());
      } else {
        errors.put("reason", "Lý do hủy không được để trống");
        throw new InvalidRequestException(errors, "Lý do hủy không được để trống");
      }
    }
    repository.insertAndUpdate(orders, true);
  }

  public Goods checkIsGoodsDisabledByBranch(String goodsId, String branchId) {
    Map<String, String> errors = generateError(OrderDetailReq.class);
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", goodsId)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
    if (1 != goods.getIsSold()) {
      errors.put("goodsId", "Sản phẩm không được mở bán");
      throw new InvalidRequestException(errors, goods.getName() + " không được mở bán");
    }
    HashMap<String, String> allParams = new HashMap<>();
    allParams.put("branchId", branchId);
    allParams.put("goodsId", goodsId);
    List<BranchGoodsDisable> list = branchGoodsDisableRepository
      .getListOrEntity(allParams, "asc", 0, 0, "")
      .get();
    if (!list.isEmpty()) {
      errors.put("goodsId", "Sản phẩm đã tạm dừng bán tại chi nhánh");
      throw new InvalidRequestException(
        errors,
        goods.getName() + " đã tạm dừng bán tại chi nhánh"
      );
    }
    return goods;
  }
}
