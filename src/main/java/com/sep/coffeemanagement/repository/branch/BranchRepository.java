package com.sep.coffeemanagement.repository.branch;

import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BranchRepository extends BaseRepository<Branch> {
  public String[] branchIgnores = { "" };

  BranchRepository() {
    this.ignores = branchIgnores;
    this.idField = "branchId";
  }

  public Optional<BranchRes> getBranchByManagerId(String managerId) {
    StringBuilder sb = new StringBuilder(" select t1.*, ");
    sb.append(
      " (select full_name from internal_user where internal_user_id = t1.branch_manager_id) branchManagerName from "
    );
    sb.append(" branch t1 ");
    sb.append(" where t1.status = 1 and t1.branch_manager_id = '");
    sb.append(managerId);
    sb.append("' ");
    Optional<List<BranchRes>> lst = replaceQuery(sb.toString(), BranchRes.class);
    if (lst.get().isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(lst.get().get(0));
  }

  public List<BranchRes> getListBranch(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb.append(
      " (select full_name from internal_user where internal_user_id = T1.branch_manager_id) branchManagerName from"
    );
    sb.append(" branch T1 ");
    sb.append(
      convertParamsFilterSelectQuery(
        allParams,
        Branch.class,
        page,
        pageSize,
        keySort,
        sortField,
        this.idField
      )
    );
    return replaceQuery(sb.toString(), BranchRes.class).get();
  }
}
