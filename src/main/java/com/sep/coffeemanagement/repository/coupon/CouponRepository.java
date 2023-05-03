package com.sep.coffeemanagement.repository.coupon;

import com.sep.coffeemanagement.dto.coupon.CouponRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CouponRepository extends BaseRepository<Coupon> {
  public String[] couponIgnores = { "" };

  CouponRepository() {
    this.ignores = couponIgnores;
    this.idField = "couponId";
  }

  public List<CouponRes> getListCouponByCartTotalPrice(
    double totalPrice,
    String couponId
  ) {
    StringBuilder sb = new StringBuilder(" SELECT * FROM ");
    sb
      .append(" coupon ")
      .append(" where status = 1 ")
      .append(" and applied_date <= curdate() ")
      .append(" and expired_date >= curdate() ")
      .append(" and max_value_promotion <=  ")
      .append(totalPrice);
    if (StringUtils.hasText(couponId)) {
      sb.append(" and coupon_id = '").append(couponId).append("'");
    }
    return replaceQuery(sb.toString(), CouponRes.class).orElse(new ArrayList<>());
  }
}
