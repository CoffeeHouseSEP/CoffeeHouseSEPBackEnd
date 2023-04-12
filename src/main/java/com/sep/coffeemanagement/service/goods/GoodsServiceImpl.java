package com.sep.coffeemanagement.service.goods;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.goods.GoodsReq;
import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.category.Category;
import com.sep.coffeemanagement.repository.category.CategoryRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.image_info.ImageInfo;
import com.sep.coffeemanagement.repository.image_info.ImageInfoRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl
  extends AbstractService<GoodsRepository>
  implements GoodsService {
  @Autowired
  private ImageInfoRepository imageInfoRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Override
  public Optional<GoodsRes> getGoods(int id) {
    return repository
      .getGoods(id)
      .map(
        goods ->
          new GoodsRes(
            goods.getGoodsId(),
            goods.getName(),
            goods.getCode(),
            goods.getApplyPrice(),
            0d,
            //                                        goods.getInnerPrice(),
            goods.getDescription(),
            goods.getStatus(),
            goods.getCategoryId(),
            goods.getIsSize(),
            goods.getIsSold(),
            goods.getIsTransfer(),
            goods.getGoodsUnit(),
            goods.getCategoryName()
          )
      );
  }

  @Override
  public Optional<ListWrapperResponse<GoodsRes>> getListGoods(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField,
    boolean isAbleToViewAll
  ) {
    List<GoodsRes> list = repository.getListGoods(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            goods ->
              new GoodsRes(
                goods.getGoodsId(),
                goods.getName(),
                goods.getCode(),
                goods.getApplyPrice(),
                isAbleToViewAll ? goods.getInnerPrice() : 0d,
                goods.getDescription(),
                goods.getStatus(),
                goods.getCategoryId(),
                goods.getIsSize(),
                goods.getIsSold(),
                isAbleToViewAll ? goods.getIsTransfer() : 0,
                goods.getGoodsUnit(),
                goods.getCategoryName()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createGoods(GoodsReq req) {
    checkValidGoodsRequest(req, false);
    Goods goods = objectMapper.convertValue(req, Goods.class);
    String newId = UUID.randomUUID().toString();

    ImageInfoReq imageReq = req.getImage();
    imageReq.setObjectId(newId);
    ImageInfo imageInfo = objectMapper.convertValue(imageReq, ImageInfo.class);
    validate(imageReq);

    //if goods is not sold then apply price = 0
    if (0 == req.getIsSold()) {
      goods.setApplyPrice(0d);
    }
    //if goods is not transfer then inner price = 0
    if (0 == req.getIsTransfer()) {
      goods.setInnerPrice(0d);
    }
    //
    goods.setGoodsId(newId);
    goods.setStatus(1);
    repository.insertAndUpdate(goods, false);
    imageInfoRepository.insertAndUpdate(imageInfo, false);
  }

  @Override
  public void updateGoods(GoodsReq req) {
    Goods goods = repository
      .getOneByAttribute("goodsId", req.getGoodsId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    checkValidGoodsRequest(req, true);
    Goods goodsUpdate = objectMapper.convertValue(req, Goods.class);
    //if goods is not sold then apply price = 0
    if (0 == req.getIsSold()) {
      goodsUpdate.setApplyPrice(0d);
    }
    //if goods is not transfer then inner price = 0
    if (0 == req.getIsTransfer()) {
      goods.setInnerPrice(0d);
    }
    //
    repository.insertAndUpdate(goodsUpdate, true);
    ImageInfo imageInfo = imageInfoRepository
      .getOneByAttribute("objectId", req.getGoodsId())
      .orElse(null);
    if (imageInfo == null) {
      imageInfo = new ImageInfo();
      imageInfo.setObjectId(req.getGoodsId());
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, false);
    } else {
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, true);
    }
  }

  private void checkValidGoodsRequest(GoodsReq req, boolean isUpdate) {
    validate(req);
    Map<String, String> errors = generateError(GoodsReq.class);
    if (1 == req.getIsSold()) {
      if (req.getApplyPrice() <= 0) {
        errors.put("applyPrice", "sold goods apply price is negative or zero");
        throw new InvalidRequestException(
          errors,
          "sold goods apply price is negative or zero"
        );
      }
    }
    if (1 == req.getIsTransfer()) {
      if (req.getInnerPrice() <= 0) {
        errors.put("innerPrice", "transfer goods inner price is negative or zero");
        throw new InvalidRequestException(
          errors,
          "transfer goods inner price is negative or zero"
        );
      }
    }
    if (
      repository.checkDuplicateFieldValue(
        "name",
        req.getName(),
        isUpdate ? req.getGoodsId() : ""
      )
    ) {
      errors.put("goodsName", "goods name duplicate");
      throw new InvalidRequestException(errors, "goods name duplicate");
    }
    if (
      repository.checkDuplicateFieldValue(
        "code",
        req.getCode(),
        isUpdate ? req.getGoodsId() : ""
      )
    ) {
      errors.put("goodsCode", "goods code duplicate");
      throw new InvalidRequestException(errors, "goods code duplicate");
    }
    Category category = categoryRepository
      .getOneByAttribute("categoryId", req.getCategoryId())
      .orElseThrow(() -> new ResourceNotFoundException("category not found"));
  }
}
