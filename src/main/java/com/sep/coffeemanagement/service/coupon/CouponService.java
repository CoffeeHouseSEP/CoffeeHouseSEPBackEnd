package com.sep.coffeemanagement.service.coupon;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponReq;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CouponService {
  Optional<ListWrapperResponse<CouponRes>> getListCoupon(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createCoupon(CouponReq req);

  void updateCoupon(CouponReq req);

  Optional<ListWrapperResponse<CouponRes>> getListCouponByCartInfo(
    List<OrderDetailReq> listOrderDetailReq
  );
}
