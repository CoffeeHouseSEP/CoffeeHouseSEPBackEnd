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
    String sortField
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
                //                                                        goods.getInnerPrice(),
                0d,
                goods.getDescription(),
                goods.getStatus(),
                goods.getCategoryId(),
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
    repository.insertAndUpdate(goodsUpdate, true);
  }

  private void checkValidGoodsRequest(GoodsReq req, boolean isUpdate) {
    validate(req);
    if (
      repository.checkDuplicateFieldValue(
        "name",
        req.getName(),
        isUpdate ? req.getGoodsId() : ""
      )
    ) {
      throw new InvalidRequestException(new HashMap<>(), "goods name duplicate");
    }
    if (
      repository.checkDuplicateFieldValue(
        "code",
        req.getCode(),
        isUpdate ? req.getGoodsId() : ""
      )
    ) {
      throw new InvalidRequestException(new HashMap<>(), "goods code duplicate");
    }
    Category category = categoryRepository
      .getOneByAttribute("categoryId", req.getCategoryId())
      .orElseThrow(() -> new ResourceNotFoundException("category not found"));
  }
}
