package com.sep.coffeemanagement.service.address;

import com.sep.coffeemanagement.dto.address.DistrictResponse;
import com.sep.coffeemanagement.dto.address.Province;
import com.sep.coffeemanagement.dto.address.ProvinceResponse;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
  @Value("classpath:Vietnam.json")
  private Resource resource;


  @Override
  public Optional<ProvinceResponse> getListAdrdress() {
    try {
      File file = resource.getFile();
      Object obj = jsonParser.parse(new FileReader(file));
      JSONObject jsonObject = (JSONObject) obj;
      ProvinceResponse provices = objectMapper.convertValue(
        jsonObject,
        ProvinceResponse.class
      );
      provices.getProvinces().stream().forEach(e -> e.setDistrict(null));
      if (provices == null) throw new ResourceNotFoundException("data is null");
      return Optional.of(provices);
    } catch (ParseException e) {
      throw new ResourceNotFoundException("parse error");
    } catch (IOException e) {
      throw new ResourceNotFoundException("I/O error");
    }
  }

  @Override
  public Optional<DistrictResponse> getListDistrctByProvince(String provinceCode) {
    try {
      Object obj = jsonParser.parse(new FileReader(resource.getFile()));
      JSONObject jsonObject = (JSONObject) obj;
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
    } catch (ParseException e) {
      throw new ResourceNotFoundException("parse error");
    } catch (IOException e) {
      throw new ResourceNotFoundException("I/O error");
    }
  }
}
