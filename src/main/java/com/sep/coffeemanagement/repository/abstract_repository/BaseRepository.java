package com.sep.coffeemanagement.repository.abstract_repository;

import com.sep.coffeemanagement.utils.StringUtils;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseRepository<T> extends AbstractRepository {
  public String[] ignores = {};
  public String idField = "";

  public Class<T> g() throws RuntimeException {
    ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();

    return (Class<T>) superclass.getActualTypeArguments()[0];
  }

  public Optional<List<T>> getListOrEntity(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sql = new StringBuilder();
    sql
      .append("SELECT * from ")
      .append(StringUtils.camelCaseToSnakeCase(g().getSimpleName()).toLowerCase())
      .append(
        convertParamsFilterSelectQuery(allParams, g(), page, pageSize, keySort, sortField)
      );
    return replaceQuery(sql.toString(), g());
  }

  public Optional<T> getOneByAttribute(String field, String value) {
    Map<String, String> condition = new HashMap<>();
    condition.put(field, value);
    List<T> list = getListOrEntity(condition, "", 0, 0, "").get();
    if (list.size() == 0) {
      return Optional.empty();
    }
    return Optional.of(list.get(0));
  }

  public void insertAndUpdate(T entity, boolean isUpdate) {
    save(entity, ignores, idField, isUpdate);
  }

  public int getTotal(Map<String, String> allParams) {
    return getTotal(allParams, g());
  }
}
