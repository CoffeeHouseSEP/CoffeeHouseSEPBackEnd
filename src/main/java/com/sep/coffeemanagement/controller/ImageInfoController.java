package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.dto.image_info.ImageInfoRes;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.service.image_info.ImageInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "image-info")
public class ImageInfoController extends AbstractController<ImageInfoService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-image-info")
  public ResponseEntity<CommonResponse<String>> addImageInfo(
    @RequestBody ImageInfoReq imageInfoReq,
    HttpServletRequest request
  ) {
    service.createImageInfo(imageInfoReq);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "save image success", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping(value = "remove-image-info")
  public ResponseEntity<CommonResponse<String>> removeImageInfo(
    @RequestParam String id,
    HttpServletRequest request
  ) {
    service.removeImageInfo(id);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "remove image success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-image-by-object-id")
  public ResponseEntity<CommonResponse<ImageInfoRes>> getImageByObjectId(
    @RequestParam String objectId
  ) {
    return response(service.getImageByObjectId(objectId), "success");
  }
}
