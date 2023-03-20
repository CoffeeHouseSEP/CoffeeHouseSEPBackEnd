package com.sep.coffeemanagement.repository.image_info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
  private int id;
  private String objectId;
  private String base64;
  private String prefix;
}
