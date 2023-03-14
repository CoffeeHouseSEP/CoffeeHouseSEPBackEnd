package com.sep.coffeemanagement.service.goods;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.goods.GoodsReq;
import com.sep.coffeemanagement.dto.goods.GoodsRes;
import java.util.Map;
import java.util.Optional;

public interface GoodsService {
  Optional<GoodsRes> getGoods(int id);

  Optional<ListWrapperResponse<GoodsRes>> getListGoods(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createGoods(GoodsReq req);

  void updateGoods(GoodsReq req);
}
