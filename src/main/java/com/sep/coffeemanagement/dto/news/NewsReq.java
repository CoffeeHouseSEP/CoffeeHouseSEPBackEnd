package com.sep.coffeemanagement.dto.news;

import java.util.Date;
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

  @NotNull(message = "title is empty or blank")
  @Length(max = 2000, message = "title over length(2000)")
  private String title;

  @NotNull(message = "content is empty or blank")
  @Length(max = 16000, message = "content over length(16000)")
  private String content;

  private String createdBy;
  private Date createdDate;
  private int status;
}
