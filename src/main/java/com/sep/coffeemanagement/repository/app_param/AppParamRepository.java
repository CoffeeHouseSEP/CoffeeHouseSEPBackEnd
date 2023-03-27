package com.sep.coffeemanagement.repository.app_param;

import com.sep.coffeemanagement.dto.app_param.AppParamRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AppParamRepository extends BaseRepository<AppParam> {
  public String[] appParamIgnores = { "" };

  AppParamRepository() {
    this.ignores = appParamIgnores;
    this.idField = "appParamId";
  }

  public List<AppParamRes> getListAppParamByParType(String parType) {
    StringBuilder sb = new StringBuilder(" select * from ");
    sb.append(" app_param ");
    sb.append(" where status = 1 and par_type like '");
    sb.append(parType);
    sb.append("' order by par_order asc ");
    return replaceQuery(sb.toString(), AppParamRes.class).get();
  }
}
