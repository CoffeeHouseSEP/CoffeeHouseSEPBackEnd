package com.sep.coffeemanagement.dto.mail;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMailDto {
  private String to;
  private String subject;
  private String content;
  private Map<String, Object> props;
}
