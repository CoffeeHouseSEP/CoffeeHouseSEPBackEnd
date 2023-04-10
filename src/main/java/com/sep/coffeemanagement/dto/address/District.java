package com.sep.coffeemanagement.dto.address;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
  private String name;
  private String pre;
  private List<Ward> ward;
  private List<String> street;
}
