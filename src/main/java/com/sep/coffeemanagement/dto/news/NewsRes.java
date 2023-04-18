package com.sep.coffeemanagement.dto.news;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRes {
  private String newsId;
  private String title;
  private String content;
  private String createdBy;
  private String createdDate;
  private int status;
  private String createdByName;
}
