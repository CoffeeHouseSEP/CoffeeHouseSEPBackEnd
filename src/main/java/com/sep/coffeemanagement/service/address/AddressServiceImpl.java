package com.sep.coffeemanagement.service.address;

import com.sep.coffeemanagement.dto.address.DistrictResponse;
import com.sep.coffeemanagement.dto.address.Province;
import com.sep.coffeemanagement.dto.address.ProvinceResponse;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl
  extends AbstractService<UserRepository>
  implements AddressService {
  private final JSONParser jsonParser = new JSONParser();
  private InputStream is;
  private Reader reader;
  private Object j;


  public AddressServiceImpl() throws IOException, ParseException {
    is = this.getClass().getResourceAsStream("/Vietnam.json");
    reader = new InputStreamReader(is, "UTF-8");
    j = jsonParser.parse(reader);
  }

  public AddressServiceImpl() throws UnsupportedEncodingException {
    is = this.getClass().getResourceAsStream("/Vietnam.json");
    reader = new InputStreamReader(is, "UTF-8");
  }

  @Override
  public Optional<ProvinceResponse> getListAdrdress() {

    JSONObject jsonObject = (JSONObject) j;
    ProvinceResponse provices = objectMapper.convertValue(
      jsonObject,
      ProvinceResponse.class
    );
    provices.getProvinces().stream().forEach(e -> e.setDistrict(null));
    if (provices == null) throw new ResourceNotFoundException("data is null");
    return Optional.of(provices);
  }

  @Override
  public Optional<DistrictResponse> getListDistrctByProvince(String provinceCode) {
    JSONObject jsonObject = (JSONObject) j;
    ProvinceResponse provices = objectMapper.convertValue(
      jsonObject,
      ProvinceResponse.class
    );
    if (provices == null) throw new ResourceNotFoundException("data is null");
    Province provinceOptional = provices
      .getProvinces()
      .stream()
      .filter(e -> provinceCode.equals(e.getCode()))
      .collect(Collectors.toList())
      .stream()
      .findFirst()
      .orElseThrow(() -> new ResourceNotFoundException("province is not found"));
    DistrictResponse districtResponse = new DistrictResponse();
    districtResponse.setDistricts(provinceOptional.getDistrict());
    return Optional.of(districtResponse);
  }
}
