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

  @NotNull(message = "Tên không được để trống")
  @NotEmpty(message = "Tên không được để trống")
  @NotBlank(message = "Tên không được để trống")
  @Length(max = 100, message = "Tên không được vượt quá 100 ký tự")
  private String name;

  @NotNull(message = "Thông tin không được để trống")
  @NotBlank(message = "Thông tin không được để trống")
  @NotEmpty(message = "Thông tin không được để trống")
  @Length(max = 4000, message = "Thông tin không được vượt quá 4000 ký tự")
  private String description;

  private int status;
}
