package com.sep.coffeemanagement.repository.order_detail;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailRepository extends BaseRepository<OrderDetail> {
  public String[] orderDetailIgnores = { "" };

  OrderDetailRepository() {
    this.ignores = orderDetailIgnores;
    this.idField = "orderDetailId";
  }

  public void removeOrderDetail(String id) {
    StringBuilder sb = new StringBuilder(
      " delete from order_detail where order_detail_id = '"
    );
    sb.append(id);
    sb.append("'");
    jdbcTemplate.execute(sb.toString());
  }
}
