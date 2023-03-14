package com.sep.coffeemanagement.repository.goods;

import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsRepository extends BaseRepository<Goods> {
  public String[] goodsIgnores = { "" };

  GoodsRepository() {
    this.ignores = goodsIgnores;
    this.idField = "goodsId";
  }

  public Optional<GoodsRes> getGoods(int id) {
    StringBuilder sb = new StringBuilder(" select t1.*, t2.name categoryName from ");
    sb.append(" goods t1 ");
    sb.append(" left join category t2 on t1.category_id = t2.category_id ");
    sb.append(" where t1.goods_id = ");
    sb.append(id);
    return Optional.of(replaceQuery(sb.toString(), GoodsRes.class).get().get(0));
  }

  public List<GoodsRes> getListGoods(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb.append(
      " (select name from category where category_id = T1.category_id) categoryName from"
    );
    sb.append(" goods T1 ");
    sb.append(
      convertParamsFilterSelectQuery(
        allParams,
        Goods.class,
        page,
        pageSize,
        keySort,
        sortField
      )
    );
    return replaceQuery(sb.toString(), GoodsRes.class).get();
  }
}
