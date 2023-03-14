package com.sep.coffeemanagement.controller;

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
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListGoods(allParams, keySort, page, pageSize, ""),
      "success"
    );
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
