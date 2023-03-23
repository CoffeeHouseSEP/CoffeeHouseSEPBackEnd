package com.sep.coffeemanagement.service.coupon;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponReq;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.coupon.Coupon;
import com.sep.coffeemanagement.repository.coupon.CouponRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl
  extends AbstractService<CouponRepository>
  implements CouponService {

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
                coupon.getType(),
                coupon.getValue(),
                coupon.getStatus(),
                coupon.getCreatedDate(),
                coupon.getExpiredDate(),
                coupon.getAppliedDate()
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
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    checkValidCouponRequest(req, true);
    repository.insertAndUpdate(objectMapper.convertValue(req, Coupon.class), true);
  }

  private void checkValidCouponRequest(CouponReq req, boolean isUpdate) {
    validate(req);
    if (
      repository.checkDuplicateFieldValue(
        "code",
        req.getCode(),
        isUpdate ? req.getCouponId() : ""
      )
    ) {
      throw new InvalidRequestException(new HashMap<>(), "coupon code duplicate");
    }
    if (req.getAppliedDate().compareTo(new Date()) <= 0) {
      throw new InvalidRequestException(
        new HashMap<>(),
        "applied date must after present"
      );
    }
    if (req.getExpiredDate().compareTo(req.getAppliedDate()) <= 0) {
      throw new InvalidRequestException(
        new HashMap<>(),
        "expired date must after applied date"
      );
    }
  }
}
