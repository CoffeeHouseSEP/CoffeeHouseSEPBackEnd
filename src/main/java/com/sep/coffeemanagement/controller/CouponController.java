package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.category.CategoryReq;
import com.sep.coffeemanagement.dto.category.CategoryRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.coupon.CouponReq;
import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.service.coupon.CouponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    try {
      service.createCoupon(couponRequest);
      return new ResponseEntity<CommonResponse<String>>(
        new CommonResponse<String>(
          true,
          null,
          "create coupon success",
          HttpStatus.OK.value()
        ),
        null,
        HttpStatus.OK.value()
      );
    } catch (Exception e) {
      return new ResponseEntity<CommonResponse<String>>(
        new CommonResponse<String>(
          true,
          null,
          "create coupon fail: " + e.getMessage(),
          HttpStatus.OK.value()
        ),
        null,
        HttpStatus.OK.value()
      );
    }
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-coupon")
  public ResponseEntity<CommonResponse<String>> updateCoupon(
    @RequestBody CouponReq couponRequest,
    HttpServletRequest request
  ) {
    //    try {
    service.updateCoupon(couponRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update coupon success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
    //    } catch (Exception e) {
    //      return new ResponseEntity<CommonResponse<String>>(
    //        new CommonResponse<String>(
    //          true,
    //          null,
    //          "update coupon fail: " + e.getMessage(),
    //          HttpStatus.OK.value()
    //        ),
    //        null,
    //        HttpStatus.OK.value()
    //      );
    //    }
  }

  @GetMapping(value = "get-list-coupon")
  public ResponseEntity<CommonResponse<ListWrapperResponse<CouponRes>>> getListCoupon(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "0") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListCoupon(allParams, keySort, page, pageSize, ""),
      "success"
    );
  }
}
