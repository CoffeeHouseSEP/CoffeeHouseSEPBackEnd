package com.sep.coffeemanagement.service.goods;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.goods.GoodsReq;
import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.dto.goods_branch.GoodsBranchRes;
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
import org.springframework.transaction.annotation.Transactional;

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
  public Optional<ListWrapperResponse<GoodsBranchRes>> getListGoodsBranchManager(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<GoodsBranchRes> list = repository
      .getListGoodsBranch(allParams, keySort, page, pageSize, sortField)
      .get();
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            goods ->
              new GoodsBranchRes(
                goods.getGoodsId(),
                goods.getName(),
                goods.getCode(),
                goods.getApplyPrice(),
                goods.getInnerPrice(),
                goods.getDescription(),
                goods.getStatus(),
                goods.getCategoryId(),
                goods.getIsSize(),
                goods.getIsSold(),
                goods.getIsTransfer(),
                goods.getGoodsUnit(),
                goods.getCategoryName(),
                goods.getIsDisabled()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        list.size()
      )
    );
  }

  @Override
  @Transactional
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
  @Transactional
  public void updateGoods(GoodsReq req) {
    Goods goods = repository
      .getOneByAttribute("goodsId", req.getGoodsId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
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
    validate(req.getImage());
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
        errors.put("applyPrice", "Giá bản của sản phẩm bán phải lớn hơn 0");
        throw new InvalidRequestException(
          errors,
          "Giá bản của sản phẩm bán phải lớn hơn 0"
        );
      }
    }
    if (1 == req.getIsTransfer()) {
      if (req.getInnerPrice() <= 0) {
        errors.put("innerPrice", "Giá nhập của sản phẩm nhập phải lớn hơn 0");
        throw new InvalidRequestException(
          errors,
          "Giá nhập của sản phẩm nhập phải lớn hơn 0"
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
      errors.put("name", "Trùng tên sản phẩm");
      throw new InvalidRequestException(errors, "Trùng tên sản phẩm");
    }
    if (
      repository.checkDuplicateFieldValue(
        "code",
        req.getCode(),
        isUpdate ? req.getGoodsId() : ""
      )
    ) {
      errors.put("code", "Trùng mã sản phẩm");
      throw new InvalidRequestException(errors, "Trùng mã sản phẩm");
    }
    Category category = categoryRepository
      .getOneByAttribute("categoryId", req.getCategoryId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
  }
}
