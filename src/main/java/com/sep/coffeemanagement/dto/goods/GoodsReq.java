package com.sep.coffeemanagement.dto.goods;

import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReq {
  private String goodsId;

  @NotNull(message = "Tên không được để trống")
  @NotEmpty(message = "Tên không được để trống")
  @NotBlank(message = "Tên không được để trống")
  @Length(max = 200, message = "Tên không được vượt quá 200 ký tự")
  private String name;

  @NotNull(message = "Mã không được để trống")
  @NotEmpty(message = "Mã không được để trống")
  @NotBlank(message = "Mã không được để trống")
  @Length(max = 200, message = "Mã không được vượt quá 200 ký tự")
  private String code;

  //  @NotNull(message = "goods apply price is empty or blank")
  //  @Positive(message = "goods apply price is negative or zero")
  private double applyPrice;

  //  @NotNull(message = "goods inner price is empty or blank")
  //  @Positive(message = "goods inner price is negative or zero")
  private double innerPrice;

  @NotNull(message = "Thông tin không được để trống")
  @NotEmpty(message = "Thông tin không được để trống")
  @NotBlank(message = "Thông tin không được để trống")
  @Length(max = 4000, message = "Thông tin không được vượt quá 4000 ký tự")
  private String description;

  private int status;

  @NotNull(message = "Danh mục không được để trống")
  private String categoryId;

  @Min(value = 0, message = "isSize must be 0 or 1")
  @Max(value = 1, message = "isSize must be 0 or 1")
  private int isSize;

  @Min(value = 0, message = "isSold must be 0 or 1")
  @Max(value = 1, message = "isSold must be 0 or 1")
  private int isSold;

  @Min(value = 0, message = "isTransfer must be 0 or 1")
  @Max(value = 1, message = "isTransfer must be 0 or 1")
  private int isTransfer;

  @NotNull(message = "Đơn vị tính không được để trống")
  @NotEmpty(message = "Đơn vị tính không được để trống")
  @NotBlank(message = "Đơn vị tính không được để trống")
  @Length(max = 50, message = "Không được quá 50 ký tự")
  private String goodsUnit;

  private ImageInfoReq image;
}
