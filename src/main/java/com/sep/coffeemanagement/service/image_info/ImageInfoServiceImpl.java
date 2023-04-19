package com.sep.coffeemanagement.service.image_info;

import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.dto.image_info.ImageInfoRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.image_info.ImageInfo;
import com.sep.coffeemanagement.repository.image_info.ImageInfoRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ImageInfoServiceImpl
  extends AbstractService<ImageInfoRepository>
  implements ImageInfoService {

  @Override
  public Optional<ImageInfoRes> getImageByObjectId(String id) {
    ImageInfo imageInfo = repository
      .getOneByAttribute("objectId", id)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
    return Optional.of(
      new ImageInfoRes(
        imageInfo.getId(),
        imageInfo.getObjectId(),
        imageInfo.getBase64(),
        imageInfo.getPrefix()
      )
    );
  }

  @Override
  public void createImageInfo(ImageInfoReq req) {
    validate(req);
    ImageInfo imageInfo = objectMapper.convertValue(req, ImageInfo.class);
    repository.insertAndUpdate(imageInfo, false);
  }

  @Override
  public void removeImageInfo(String id) {
    ImageInfo imageInfo = repository
      .getOneByAttribute("id", id)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
    repository.removeImageInfo(id);
  }
}
