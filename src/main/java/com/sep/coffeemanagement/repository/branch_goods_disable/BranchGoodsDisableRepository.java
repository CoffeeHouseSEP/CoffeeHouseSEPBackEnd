package com.sep.coffeemanagement.repository.branch_goods_disable;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BranchGoodsDisableRepository extends BaseRepository<BranchGoodsDisable> {
  public String[] branchGoodsDisableIgnores = { "" };

  BranchGoodsDisableRepository() {
    this.ignores = branchGoodsDisableIgnores;
    this.idField = "id";
  }

  public void removeBranchGoodsDisable(int id) {
    StringBuilder sb = new StringBuilder(" delete from branch_goods_disable where id = ");
    sb.append(id);
    jdbcTemplate.execute(sb.toString());
  }
}
