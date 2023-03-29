package com.sep.coffeemanagement.repository.request_detail;

import com.sep.coffeemanagement.dto.request_detail.RequestDetailRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RequestDetailRepository extends BaseRepository<RequestDetail> {
  public String[] requestDetailIgnores = { "" };

  RequestDetailRepository() {
    this.ignores = requestDetailIgnores;
    this.idField = "requestDetailId";
  }

  public void removeRequestDetailByRequestId(String requestId) {
    StringBuilder sb = new StringBuilder(
      " delete from request_detail where request_id = '"
    );
    sb.append(requestId);
    sb.append("'");
    jdbcTemplate.execute(sb.toString());
  }

  public List<RequestDetailRes> getListRequestDetailByRequestId(String requestId) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb.append(" (select name from goods where goods_id = T1.goods_id) goodsName from");
    sb.append(" request_detail T1 where request_id = '");
    sb.append(requestId);
    sb.append("'");
    return replaceQuery(sb.toString(), RequestDetailRes.class)
      .orElseThrow(() -> new ResourceNotFoundException("no content"));
  }
}
