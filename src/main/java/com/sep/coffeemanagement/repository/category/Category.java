package com.sep.coffeemanagement.repository.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  private String categoryId;
  private String name;
  private String description;
  private int status;
}
