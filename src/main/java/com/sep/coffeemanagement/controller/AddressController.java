package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.address.DistrictResponse;
import com.sep.coffeemanagement.dto.address.ProvinceResponse;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.service.address.AddressService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("address")
public class AddressController extends AbstractController<AddressService> {

  @GetMapping(value = "get-list-province")
  public ResponseEntity<CommonResponse<ProvinceResponse>> getListProvince(
    HttpServletRequest request
  ) {
    return response(service.getListAdrdress(), "Thành công");
  }

  @GetMapping(value = "get-list-district-by-province-code")
  public ResponseEntity<CommonResponse<DistrictResponse>> getListDistrictByProvinceCode(
    @RequestParam @NotNull(
      message = "Tỉnh/Thành phố không được để trống"
    ) String provinceCode,
    HttpServletRequest request
  ) {
    return response(service.getListDistrctByProvince(provinceCode), "Thành công");
  }
}
