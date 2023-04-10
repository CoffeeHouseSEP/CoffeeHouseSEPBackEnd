package com.sep.coffeemanagement.dto.goods;

import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReq {
  private String goodsId;

  @NotNull(message = "goods name is empty or blank")
  @Length(max = 200, message = "goods name over length(200)")
  private String name;

  @NotNull(message = "goods code is empty or blank")
  @Length(max = 200, message = "goods code over length(200)")
  private String code;

  //  @NotNull(message = "goods apply price is empty or blank")
  //  @Positive(message = "goods apply price is negative or zero")
  private double applyPrice;

  //  @NotNull(message = "goods inner price is empty or blank")
  //  @Positive(message = "goods inner price is negative or zero")
  private double innerPrice;

  @NotNull(message = "description is empty or blank")
  @Length(max = 4000, message = "description over length(4000)")
  private String description;

  private int status;

  @NotNull(message = "category is empty or blank")
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

  private String goodsUnit;
  private ImageInfoReq image;
}
