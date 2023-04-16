package com.sep.coffeemanagement.repository.request;

import com.sep.coffeemanagement.dto.request.RequestRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import com.sep.coffeemanagement.repository.news.News;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

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
}
