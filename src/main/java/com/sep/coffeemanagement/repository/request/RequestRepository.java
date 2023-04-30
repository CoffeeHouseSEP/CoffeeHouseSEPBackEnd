package com.sep.coffeemanagement.repository.request;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.request.RequestRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import com.sep.coffeemanagement.repository.news.News;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class RequestRepository extends BaseRepository<Request> {
  public String[] requestIgnores = { "" };

  RequestRepository() {
    this.ignores = requestIgnores;
    this.idField = "requestId";
  }

  public List<RequestRes> getListRequest(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb
      .append(
        " (select full_name from internal_user where internal_user_id = T1.created_by) createdByName, "
      )
      .append(
        " (select full_name from internal_user where internal_user_id = T1.approved_by) approvedByName, "
      )
      .append(" (select name from branch where branch_id = T1.branch_id) branchName ")
      .append(" from request T1 ")
      .append(
        convertParamsFilterSelectQuery(
          allParams,
          Request.class,
          page,
          pageSize,
          keySort,
          sortField,
          this.idField
        )
      );
    return replaceQuery(sb.toString(), RequestRes.class).get();
  }

  public Optional<List<Request>> getListIncompleteRequestInBranch(
    String branchId,
    String exceptId
  ) {
    StringBuilder sb = new StringBuilder(" select ");
    sb.append(" * from request where ");
    sb.append(" branch_id = '");
    sb.append(branchId);
    sb.append("'");
    sb.append(" and ");
    sb.append(" status not in (");
    sb.append("'");
    sb.append(Constant.REQUEST_STATUS.COMPLETED.toString());
    sb.append("',");
    sb.append("'");
    sb.append(Constant.REQUEST_STATUS.CANCELLED.toString());
    sb.append("')");
    if (StringUtils.hasText(exceptId)) {
      sb.append(" and ");
      sb.append(" request_id ");
      sb.append(" NOT LIKE '");
      sb.append(exceptId);
      sb.append("'");
    }
    return replaceQuery(sb.toString(), Request.class);
  }
}
