package com.sep.coffeemanagement.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRes {
  private String categoryId;
  private String name;
  private String description;
  private int status;
}
