package com.sep.coffeemanagement.service.address;

import com.sep.coffeemanagement.dto.address.District;
import com.sep.coffeemanagement.dto.address.DistrictResponse;
import com.sep.coffeemanagement.dto.address.ProvinceResponse;
import java.util.Optional;

public interface AddressService {
  Optional<ProvinceResponse> getListAdrdress();
  Optional<DistrictResponse> getListDistrctByProvince(String provinceCode);
}
