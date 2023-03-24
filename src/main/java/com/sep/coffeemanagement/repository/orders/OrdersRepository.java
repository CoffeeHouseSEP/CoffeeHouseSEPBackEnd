package com.sep.coffeemanagement.repository.orders;

import com.sep.coffeemanagement.dto.orders.OrdersRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepository extends BaseRepository<Orders> {
  public String[] ordersIgnores = { "" };

  OrdersRepository() {
    this.ignores = ordersIgnores;
    this.idField = "ordersId";
  }

  public List<OrdersRes> getListOrders(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb
      .append(
        " (select login_name from customer where customer_id = T1.customer_id) customerName "
      )
      .append(" ,(select name from branch where branch_id = T1.branch_id) branchName ")
      .append(" ,(select code from coupon where coupon_id = T1.coupon_id) couponCode ");
    sb.append(" from orders T1 ");
    sb.append(
      convertParamsFilterSelectQuery(
        allParams,
        Orders.class,
        page,
        pageSize,
        keySort,
        sortField,
        this.idField
      )
    );
    return replaceQuery(sb.toString(), OrdersRes.class).get();
  }
}
