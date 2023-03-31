package com.sep.coffeemanagement.repository.order_detail;

import com.sep.coffeemanagement.dto.order_detail.OrderDetailRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailRepository extends BaseRepository<OrderDetail> {
  public String[] orderDetailIgnores = { "" };

  OrderDetailRepository() {
    this.ignores = orderDetailIgnores;
    this.idField = "orderDetailId";
  }

  public void removeOrderDetailByOrdersId(String ordersId) {
    StringBuilder sb = new StringBuilder(" delete from order_detail where orders_id = '");
    sb.append(ordersId);
    sb.append("'");
    jdbcTemplate.execute(sb.toString());
  }

  public List<OrderDetailRes> getListOrderDetailByOrdersId(String ordersId) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb.append(" (select name from goods where goods_id = T1.goods_id) goodsName ");
    sb.append(" from order_detail T1 ");
    sb.append(" where T1.orders_id = '");
    sb.append(ordersId);
    sb.append("'");
    return replaceQuery(sb.toString(), OrderDetailRes.class)
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
  }
}
