package com.sep.coffeemanagement.repository.goods;

import com.sep.coffeemanagement.dto.goods.GoodsRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsRepository extends BaseRepository<Goods> {
  public String[] goodsIgnores = { "goodsId" };

  GoodsRepository() {
    this.ignores = goodsIgnores;
    this.idField = "goodsId";
  }

  public Optional<GoodsRes> getGoods(int id) {
    StringBuilder sb = new StringBuilder(
      //      " SELECT T1.*, T2.NAME categoryName, T3.FILE_PATH base64String FROM "
      " select t1.*, t2.name categoryName FROM "
    );
    sb.append(
      //      " GOODS T1 LEFT JOIN UTIL_ATTACH T3 ON T1.GOODS_ID = T3.OBJECT_ID AND T3.TYPE = 'GOODS_IMAGE' "
      " goods t1  "
    );
    sb.append(" left join category t2 ON t1.category_id = t2.category_id ");
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
    StringBuilder sb = new StringBuilder(
      //      " SELECT T1.*, T2.NAME categoryName, T3.FILE_PATH base64String FROM"
      " SELECT T1.*, T2.name categoryName FROM"
    );
    sb.append(
      //      " GOODS T1 LEFT JOIN UTIL_ATTACH T3 ON T1.GOODS_ID = T3.OBJECT_ID AND T3.TYPE = 'GOODS_IMAGE' "
      " goods T1 "
    );
    sb.append(" left join category T2 ON T1.category_id = T2.category_id ");
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
