package com.sep.coffeemanagement.repository.abstract_repository;

import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.utils.StringUtils;
import java.lang.reflect.Field;
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
        convertParamsFilterSelectQuery(
          allParams,
          g(),
          page,
          pageSize,
          keySort,
          sortField,
          idField
        )
      );
    return replaceQuery(sql.toString(), g());
  }

  public Optional<T> getOneByAttribute(String field, String value) {
    int countFieldName = 0;
    Field[] fields = g().getDeclaredFields();
    for (Field declaredField : fields) {
      if (declaredField.getName().compareTo(field) == 0) {
        countFieldName++;
      }
    }
    if (countFieldName == 0) {
      return Optional.empty();
    }
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
    return getTotal(allParams, g(), idField);
  }

  //duongnt: only for check nvarchar field (name, code, etc ..)
  //return true if duplicate
  public boolean checkDuplicateFieldValue(
    String fieldName,
    String value,
    String exceptId
  ) {
    StringBuilder sb = new StringBuilder("SELECT * FROM ");
    sb
      .append(StringUtils.camelCaseToSnakeCase(g().getSimpleName()).toLowerCase())
      .append(" WHERE ")
      .append(fieldName)
      .append(" LIKE '")
      .append(value)
      .append("'");
    if (org.springframework.util.StringUtils.hasText(exceptId)) {
      sb
        .append("AND ")
        .append(StringUtils.camelCaseToSnakeCase(g().getSimpleName()).toLowerCase())
        .append("_id NOT LIKE '")
        .append(exceptId)
        .append("'");
    }
    List<T> lst = replaceQuery(sb.toString(), g())
      .orElseThrow(() -> new ResourceNotFoundException("list object not found!"));
    return !lst.isEmpty();
  }
}
