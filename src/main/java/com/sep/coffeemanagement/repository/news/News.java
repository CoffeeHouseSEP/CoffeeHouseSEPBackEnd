package com.sep.coffeemanagement.repository.news;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
  private String newsId;
  private String title;
  private String content;
  private String createdBy;
  private Date createdDate;
  private int status;
}
