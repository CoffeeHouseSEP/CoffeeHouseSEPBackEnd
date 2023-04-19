package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.category.CategoryReq;
import com.sep.coffeemanagement.dto.category.CategoryRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponReq;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import com.sep.coffeemanagement.repository.order_detail.OrderDetail;
import com.sep.coffeemanagement.service.coupon.CouponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "coupon")
public class CouponController extends AbstractController<CouponService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-coupon")
  public ResponseEntity<CommonResponse<String>> addNewCoupon(
    @RequestBody CouponReq couponRequest,
    HttpServletRequest request
  ) {
    service.createCoupon(couponRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "Tạo mới thành công", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-coupon")
  public ResponseEntity<CommonResponse<String>> updateCoupon(
    @RequestBody CouponReq couponRequest,
    HttpServletRequest request
  ) {
    service.updateCoupon(couponRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Cập nhật thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @GetMapping(value = "get-list-coupon")
  public ResponseEntity<CommonResponse<ListWrapperResponse<CouponRes>>> getListCoupon(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String role = getUserRoleByRequest(request);
    if (!Constant.ADMIN_ROLE.equals(role)) {
      allParams.put("status", "1");
    }
    return response(
      service.getListCoupon(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @PostMapping(value = "get-list-coupon-by-cart-info")
  public ResponseEntity<CommonResponse<ListWrapperResponse<CouponRes>>> getListCouponByCartInfo(
    @RequestBody List<OrderDetailReq> listOrderDetailReq,
    HttpServletRequest request
  ) {
    return response(service.getListCouponByCartInfo(listOrderDetailReq), "Thành công");
  }
}
