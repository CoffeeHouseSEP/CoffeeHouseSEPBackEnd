package com.sep.coffeemanagement.service.coupon;

import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponReq;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.app_param.AppParamRepository;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.AppParamUtils;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl
  extends AbstractService<CouponRepository>
  implements CouponService {
  @Autowired
  private GoodsRepository goodsRepository;

  @Autowired
  private AppParamRepository appParamRepository;

  @Override
  public Optional<ListWrapperResponse<CouponRes>> getListCoupon(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<Coupon> list = repository
      .getListOrEntity(allParams, keySort, page, pageSize, sortField)
      .get();
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            coupon ->
              new CouponRes(
                coupon.getCouponId(),
                coupon.getCode(),
                coupon.getValue(),
                coupon.getStatus(),
                DateFormat.toDateString(coupon.getCreatedDate(), DateTime.YYYY_MM_DD),
                DateFormat.toDateString(coupon.getExpiredDate(), DateTime.YYYY_MM_DD),
                DateFormat.toDateString(coupon.getAppliedDate(), DateTime.YYYY_MM_DD),
                coupon.getMaxValuePromotion()
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
  public void createCoupon(CouponReq req) {
    checkValidCouponRequest(req, false);
    Coupon coupon = objectMapper.convertValue(req, Coupon.class);
    String newId = UUID.randomUUID().toString();
    coupon.setCouponId(newId);
    coupon.setStatus(1);
    coupon.setCreatedDate(DateFormat.getCurrentTime());
    repository.insertAndUpdate(coupon, false);
  }

  @Override
  public void updateCoupon(CouponReq req) {
    Coupon coupon = repository
      .getOneByAttribute("couponId", req.getCouponId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá"));
    checkValidCouponRequest(req, true);
    repository.insertAndUpdate(objectMapper.convertValue(req, Coupon.class), true);
  }

  private void checkValidCouponRequest(CouponReq req, boolean isUpdate) {
    validate(req);
    Map<String, String> errors = generateError(CouponReq.class);
    if (
      repository.checkDuplicateFieldValue(
        "code",
        req.getCode(),
        isUpdate ? req.getCouponId() : ""
      )
    ) {
      errors.put("code", "Trùng mã giảm giá");
      throw new InvalidRequestException(errors, "Trùng mã giảm giá");
    }
    if (req.getAppliedDate().compareTo(new Date()) <= 0) {
      errors.put("appliedDate", "Ngày áp dụng phải sau ngày hiện tại");
      throw new InvalidRequestException(errors, "Ngày áp dụng phải sau ngày hiện tại");
    }
    if (req.getExpiredDate().compareTo(req.getAppliedDate()) <= 0) {
      errors.put("expiredDate", "Ngày hết hạn phải sau ngày áp dụng");
      throw new InvalidRequestException(errors, "Ngày hết hạn phải sau ngày áp dụng");
    }
  }

  @Override
  public Optional<ListWrapperResponse<CouponRes>> getListCouponByCartInfo(
    List<OrderDetailReq> listOrderDetailReq
  ) {
    if (listOrderDetailReq == null || listOrderDetailReq.isEmpty()) {
      throw new ResourceNotFoundException("Thông tin giỏ hàng trống");
    }
    List<AppParamRes> listGoodsSize = appParamRepository.getListAppParamByParType(
      "UPSIZE_GOODS"
    );
    double totalPrice = 0;
    for (OrderDetailReq orderDetailReq : listOrderDetailReq) {
      validate(orderDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", orderDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
      totalPrice +=
        orderDetailReq.getQuantity() *
        goods.getApplyPrice() *
        AppParamUtils.getRatioValueFromListSizeGoods(
          listGoodsSize,
          orderDetailReq.getSize()
        );
    }
    List<CouponRes> list = repository.getListCouponByCartTotalPrice(totalPrice, null);
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            coupon ->
              new CouponRes(
                coupon.getCouponId(),
                coupon.getCode(),
                coupon.getValue(),
                coupon.getStatus(),
                coupon.getCreatedDate(),
                coupon.getExpiredDate(),
                coupon.getAppliedDate(),
                coupon.getMaxValuePromotion()
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
