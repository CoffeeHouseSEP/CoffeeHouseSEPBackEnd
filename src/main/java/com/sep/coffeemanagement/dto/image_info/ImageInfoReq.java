package com.sep.coffeemanagement.dto.image_info;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfoReq {
  private int id;

  @NotNull(message = "object is undefined")
  private String objectId;

  @NotNull(message = "content is empty or blank")
  @Length(message = "content over flow")
  private String base64;

  @NotNull(message = "prefix is empty or blank")
  private String prefix;
}
