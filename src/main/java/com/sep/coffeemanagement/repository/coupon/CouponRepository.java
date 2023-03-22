package com.sep.coffeemanagement.repository.coupon;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository extends BaseRepository<Coupon> {
  public String[] couponIgnores = { "" };

  CouponRepository() {
    this.ignores = couponIgnores;
    this.idField = "couponId";
  }
}
