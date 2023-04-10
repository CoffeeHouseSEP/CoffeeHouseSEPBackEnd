package com.sep.coffeemanagement.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReq {
  private String categoryId;

  @NotNull(message = "category name is null")
  @NotEmpty(message = "category name is empty")
  @NotBlank(message = "category name is blank")
  @Length(max = 100, message = "category name over length(100)")
  private String name;

  @NotNull(message = "category description is null")
  @NotBlank(message = "category description is blank")
  @NotEmpty(message = "category description is empty")
  @Length(max = 4000, message = "category description over length(4000)")
  private String description;

  private int status;
}
