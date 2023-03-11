package com.sep.coffeemanagement.service.goods;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.goods.GoodsReq;
import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl
  extends AbstractService<GoodsRepository>
  implements GoodsService {

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
    validate(req);
    Goods goods = objectMapper.convertValue(req, Goods.class);
    goods.setGoodsId(0);
    goods.setStatus(1);
    repository.insertAndUpdate(goods, false);
  }

  @Override
  public void updateGoods(GoodsReq req) {
    Goods goods = repository
      .getOneByAttribute("goodsId", Integer.toString(req.getGoodsId()))
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    Goods goodsUpdate = objectMapper.convertValue(req, Goods.class);
    validate(goodsUpdate);
    repository.insertAndUpdate(goodsUpdate, true);
  }
}
