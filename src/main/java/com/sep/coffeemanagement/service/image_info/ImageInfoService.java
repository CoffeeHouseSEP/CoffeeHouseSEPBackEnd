package com.sep.coffeemanagement.service.image_info;

import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.dto.image_info.ImageInfoRes;
import java.util.Optional;

public interface ImageInfoService {
  Optional<ImageInfoRes> getImageByObjectId(String id);

  void createImageInfo(ImageInfoReq req);

  void removeImageInfo(String id);
}
