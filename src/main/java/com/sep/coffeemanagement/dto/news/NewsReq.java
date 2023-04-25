package com.sep.coffeemanagement.dto.news;

import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import java.util.Date;
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
public class NewsReq {
  private String newsId;

  @NotNull(message = "Tiêu đề không được để trống")
  @NotEmpty(message = "Tiêu đề không được để trống")
  @NotBlank(message = "Tiêu đề không được để trống")
  @Length(max = 2000, message = "Tiêu đề không được vượt qu 2000 ký tự")
  private String title;

  @NotNull(message = "Nội dung không được để trống")
  @NotEmpty(message = "Nội dung không được để trống")
  @NotBlank(message = "Nội dung không được để trống")
  @Length(max = 16000, message = "Nội dung không được vượt quá 16000 ký tự")
  private String content;

  private String createdBy;
  private int status;
  private ImageInfoReq image;
}
