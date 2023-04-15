package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.goods.GoodsReq;
import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.service.goods.GoodsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "goods")
public class GoodsController extends AbstractController<GoodsService> {

  //unauthen
  @GetMapping(value = "get-list-goods")
  public ResponseEntity<CommonResponse<ListWrapperResponse<GoodsRes>>> getListGoods(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    allParams.put("status", "1");
    allParams.put("isSold", "1");
    return response(
      service.getListGoods(allParams, keySort, page, pageSize, sortField, false),
      "success"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-goods-authorized")
  public ResponseEntity<CommonResponse<ListWrapperResponse<GoodsRes>>> getListGoodsAuthorize(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String role = getUserRoleByRequest(request);
    if (Constant.BRANCH_ROLE.equals(role)) {
      allParams.put("status", "1");
      return response(
        service.getListGoods(allParams, keySort, page, pageSize, sortField, true),
        "success"
      );
    } else if (Constant.ADMIN_ROLE.equals(role)) {
      return response(
        service.getListGoods(allParams, keySort, page, pageSize, sortField, true),
        "success"
      );
    } else {
      allParams.put("status", "1");
      allParams.put("isSold", "1");
      return response(
        service.getListGoods(allParams, keySort, page, pageSize, sortField, false),
        "success"
      );
    }
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-goods")
  public ResponseEntity<CommonResponse<String>> addNewGoods(
    @RequestBody GoodsReq goodsRequest,
    HttpServletRequest request
  ) {
    service.createGoods(goodsRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "create goods success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-goods")
  public ResponseEntity<CommonResponse<String>> updateGoods(
    @RequestBody GoodsReq goodsRequest,
    HttpServletRequest request
  ) {
    service.updateGoods(goodsRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update goods success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
