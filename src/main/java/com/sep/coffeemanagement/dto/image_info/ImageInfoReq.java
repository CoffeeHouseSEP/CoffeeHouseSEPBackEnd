package com.sep.coffeemanagement.dto.image_info;

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
public class ImageInfoReq {
  private int id;

  private String objectId;

  @NotNull(message = "Không đọc được ảnh")
  @NotBlank(message = "Không đọc được ảnh")
  @NotEmpty(message = "Không đọc được ảnh")
  @Length(message = "Ảnh vượt quá dung lượng cho phép")
  private String base64;

  @NotBlank(message = "File extension không xác định")
  @NotEmpty(message = "File extension không xác định")
  @NotNull(message = "File extension không xác định")
  private String prefix;
}
