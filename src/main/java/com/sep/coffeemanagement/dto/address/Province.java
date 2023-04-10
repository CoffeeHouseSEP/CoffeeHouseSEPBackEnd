package com.sep.coffeemanagement.dto.address;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Province {
  private String code;
  private String name;
  private List<District> district;
}
