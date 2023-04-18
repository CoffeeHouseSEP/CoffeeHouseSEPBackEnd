package com.sep.coffeemanagement.repository.abstract_repository;

import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.exception.BadSqlException;
import com.sep.coffeemanagement.log.AppLogger;
import com.sep.coffeemanagement.log.LoggerFactory;
import com.sep.coffeemanagement.log.LoggerType;
import com.sep.coffeemanagement.utils.StringUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractRepository {
  @Autowired
  @Qualifier("MainJdbcTemplate")
  protected JdbcTemplate jdbcTemplate;

  protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

  protected boolean isFirstCondition = true;

  protected int hasAnd = 0;

  protected int counting = 0;

  protected int maxLength = 0;

  protected <T> Optional<List<T>> replaceQuery(String sql, Class<T> clazz) {
    try {
      List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(clazz));
      return Optional.of(list);
    } catch (BadSqlGrammarException | NoSuchElementException e) {
      APP_LOGGER.error("ERROR SQL QUERY: " + sql);
      return Optional.of(new ArrayList<>());
    }
  }

  protected int getMax(String sql) {
    try {
      int result = jdbcTemplate.queryForObject(sql, Integer.class);
      return result;
    } catch (NullPointerException e) {
      APP_LOGGER.error(e.getMessage());
      return 0;
    }
  }

  protected <T> void save(
    T entity,
    String[] ignoreFields,
    String idField,
    boolean isUpdate
  ) {
    StringBuilder sql = new StringBuilder();
    if (isUpdate) {
      try {
        Field fieldId = entity.getClass().getDeclaredField(idField);
        fieldId.setAccessible(true);
        sql.append(
          "UPDATE " +
          StringUtils
            .camelCaseToSnakeCase(entity.getClass().getSimpleName())
            .toLowerCase() +
          " SET "
        );
        Field[] fields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
          fields[i].setAccessible(true);
          if (fields[i].getType() == String.class) {
            try {
              if (fields[i].get(entity) == null) {
                sql.append(
                  StringUtils.camelCaseToSnakeCase(fields[i].getName()) + "=null"
                );
              } else {
                sql.append(
                  StringUtils.camelCaseToSnakeCase(fields[i].getName()) +
                  "=" +
                  "'" +
                  fields[i].get(entity) +
                  "'"
                );
              }
            } catch (IllegalArgumentException | IllegalAccessException e1) {
              APP_LOGGER.error(
                "Not found " + fields[i].getName() + " in " + entity.getClass().getName()
              );
              throw new BadSqlException("something went wrong");
            }
          }
          //anhpd modify
          if (fields[i].getType() == int.class || fields[i].getType() == double.class) {
            try {
              sql.append(
                StringUtils.camelCaseToSnakeCase(fields[i].getName()) +
                "=" +
                fields[i].get(entity)
              );
            } catch (IllegalArgumentException | IllegalAccessException e1) {
              APP_LOGGER.error(
                "Not found " + fields[i].getName() + " in " + entity.getClass().getName()
              );
              throw new BadSqlException("something went wrong");
            }
          }
          if (fields[i].getType() == Date.class) {
            try {
              Date date = (Date) fields[i].get(entity);
              if (date == null) {
                sql.append(
                  StringUtils.camelCaseToSnakeCase(fields[i].getName()) + "=null"
                );
              } else {
                sql.append(
                  StringUtils.camelCaseToSnakeCase(fields[i].getName()) +
                  "=" +
                  "'" +
                  new java.sql.Date(date.getTime()) +
                  "'"
                );
              }
            } catch (IllegalArgumentException | IllegalAccessException e1) {
              APP_LOGGER.error(
                "Not found " + fields[i].getName() + " in " + entity.getClass().getName()
              );
              throw new BadSqlException("something went wrong");
            }
          }
          if (i != fields.length - 1) {
            sql.append(",");
          }
        }
        sql.append(
          generateConditionInQuery(fieldId, fieldId.get(entity).toString(), idField)
        );
      } catch (
        EmptyResultDataAccessException
        | NoSuchFieldException
        | IllegalArgumentException
        | IllegalAccessException e
      ) {
        APP_LOGGER.error("BAD SQL: " + sql.toString());
        e.printStackTrace();
        throw new BadSqlException("something went wrong");
      }
    } else {
      try {
        StringBuilder fieldInsert = new StringBuilder();
        StringBuilder valueInsert = new StringBuilder();
        Field[] fields = entity.getClass().getDeclaredFields();
        sql.append(
          "INSERT INTO " +
          StringUtils
            .camelCaseToSnakeCase(entity.getClass().getSimpleName())
            .toLowerCase()
        );
        fieldInsert.append(" (");
        valueInsert.append(" (");
        for (int i = 0; i < fields.length; i++) {
          boolean isInsert = true;
          for (String ignore : ignoreFields) {
            if (fields[i].getName().compareTo(ignore) == 0) {
              isInsert = false;
              break;
            }
          }
          if (isInsert) {
            fields[i].setAccessible(true);
            fieldInsert.append(StringUtils.camelCaseToSnakeCase(fields[i].getName()));
            if (fields[i].getType() == String.class) {
              try {
                if (fields[i].get(entity) == null) {
                  valueInsert.append("null");
                } else {
                  valueInsert.append("'" + fields[i].get(entity) + "'");
                }
              } catch (IllegalArgumentException | IllegalAccessException e1) {
                APP_LOGGER.error(
                  "Not found " +
                  fields[i].getName() +
                  " in " +
                  entity.getClass().getName()
                );
                throw new BadSqlException("something went wrong");
              }
            }
            //anhpd modify
            if (fields[i].getType() == int.class || fields[i].getType() == double.class) {
              try {
                valueInsert.append(fields[i].get(entity));
              } catch (IllegalArgumentException | IllegalAccessException e1) {
                APP_LOGGER.error(
                  "Not found " +
                  fields[i].getName() +
                  " in " +
                  entity.getClass().getName()
                );
                throw new BadSqlException("something went wrong");
              }
            }
            if (fields[i].getType() == Date.class) {
              try {
                Date date = (Date) fields[i].get(entity);
                if (date == null) {
                  valueInsert.append("null");
                } else {
                  valueInsert.append("'" + new java.sql.Date(date.getTime()) + "'");
                }
              } catch (IllegalArgumentException | IllegalAccessException e1) {
                APP_LOGGER.error(
                  "Not found " +
                  fields[i].getName() +
                  " in " +
                  entity.getClass().getName()
                );
                throw new BadSqlException("something went wrong");
              }
            }
            if (i != fields.length - 1) {
              fieldInsert.append(",");
              valueInsert.append(",");
            }
          }
        }
        fieldInsert.append(")");
        valueInsert.append(")");
        sql.append(fieldInsert.toString() + " VALUES " + valueInsert.toString());
      } catch (
        EmptyResultDataAccessException
        | IllegalArgumentException
        | BadSqlGrammarException e
      ) {
        APP_LOGGER.info(e.getLocalizedMessage());
        throw new BadSqlException("something went wrong");
      }
    }
    try {
      counting = 0;
      hasAnd = 0;
      maxLength = 0;
      isFirstCondition = true;
      jdbcTemplate.execute(sql.toString());
    } catch (BadSqlGrammarException e) {
      APP_LOGGER.error("BAD SQL: " + sql.toString());
      throw new BadSqlException("something went wrong");
    }
  }

  protected String insertFirstCondition(String type) {
    if (isFirstCondition) {
      isFirstCondition = false;
      return " WHERE ";
    }
    if (hasAnd > 1 && counting != maxLength - 1) return " AND ";
    return " OR ";
  }

  protected String generateConditionInQuery(Field field, String value, String fieldId) {
    StringBuilder result = new StringBuilder();
    if (field.getName().compareTo(fieldId) == 0) {
      result.append(insertFirstCondition(""));
      result
        .append(StringUtils.camelCaseToSnakeCase(field.getName()))
        .append(" = '")
        .append(value.toLowerCase())
        .append("'");
    } else if (field.getType() == String.class) {
      result.append(insertFirstCondition(""));
      result
        .append(StringUtils.camelCaseToSnakeCase(field.getName()))
        .append(" LIKE '%")
        .append(value.toLowerCase())
        .append("%'");
    } else if (field.getType() == int.class) {
      try {
        int valueParse = Integer.parseInt(value.toLowerCase());
        result.append(insertFirstCondition(""));
        result
          .append(StringUtils.camelCaseToSnakeCase(field.getName()))
          .append(" = ")
          .append(valueParse);
      } catch (NumberFormatException e) {
        APP_LOGGER.error("error parse number: " + field.getName() + " = " + value);
      }
    } else if (field.getType() == Date.class) {
      if (value.matches(TypeValidation.DATE)) {
        result.append(insertFirstCondition(""));
        result
          .append(StringUtils.camelCaseToSnakeCase(field.getName()))
          .append(" = ")
          .append("'" + value + "'");
      }
    }
    return result.toString();
  }

  protected String generateDateCondition(Field field, String[] values) {
    StringBuilder result = new StringBuilder();
    if (values.length >= 2) {
      int max = values.length - values.length % 2;
      for (int i = 0; i < max; i += 2) {
        if (
          values[i].matches(TypeValidation.DATE) &&
          values[i + 1].matches(TypeValidation.DATE)
        ) {
          result.append(insertFirstCondition("date"));
          result.append(
            StringUtils.camelCaseToSnakeCase(field.getName()) + " >= '" + values[i] + "'"
          );
          result.append(" AND ");
          result.append(
            StringUtils.camelCaseToSnakeCase(field.getName()) +
            " <= '" +
            values[i + 1] +
            "'"
          );
        }
      }
    }
    return result.toString();
  }

  protected String convertParamsFilterSelectQuery(
    Map<String, String> allParams,
    Class<?> clazz,
    int page,
    int pageSize,
    String keySort,
    String sortField,
    String fieldId
  ) {
    Field[] fields = clazz.getDeclaredFields();
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, String> items : allParams.entrySet()) {
      maxLength = fields.length;
      for (Field field : fields) {
        if (field.getName().compareTo(items.getKey()) == 0) {
          hasAnd++;
        }
      }
      for (Field field : fields) {
        if (field.getName().compareTo(items.getKey()) == 0) {
          String[] values = items.getValue().split(",");
          if (values.length != 0) {
            if (field.getType() == Date.class) {
              result.append(generateDateCondition(field, values));
            } else {
              for (int i = 0; i < values.length; i++) {
                result.append(generateConditionInQuery(field, values[i], fieldId));
              }
            }
          }
        }
      }
      counting++;
    }
    counting = 0;
    hasAnd = 0;
    maxLength = 0;
    isFirstCondition = true;
    if (
      !org.springframework.util.StringUtils.hasText(keySort) |
      !org.springframework.util.StringUtils.hasText(sortField)
    ) {} else if (
      !org.springframework.util.StringUtils.hasText(keySort) &&
      !org.springframework.util.StringUtils.hasText(sortField)
    ) {} else {
      result
        .append(" ORDER BY ")
        .append(StringUtils.camelCaseToSnakeCase(sortField))
        .append(" ")
        .append(keySort);
    }
    if (pageSize != 0) {
      result
        .append(" LIMIT ")
        .append(pageSize)
        .append(" OFFSET ")
        .append((page - 1) * pageSize);
    }
    return result.toString();
  }

  protected int getTotal(Map<String, String> allParams, Class<?> clazz, String fieldId) {
    StringBuilder sql = new StringBuilder();
    System.out.println(clazz.getSimpleName());
    sql.append(
      "SELECT COUNT(*) FROM " + StringUtils.camelCaseToSnakeCase(clazz.getSimpleName())
    );
    sql.append(convertParamsFilterSelectQuery(allParams, clazz, 0, 0, "", "", fieldId));
    try {
      int result = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
      return result;
    } catch (NullPointerException e) {
      APP_LOGGER.error(e.getMessage());
      return 0;
    } catch (BadSqlGrammarException e) {
      APP_LOGGER.error("Bad SQL: " + sql.toString());
      return 0;
    }
  }

  protected List<String> listAttributeName(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
  }

  protected String attributeNamesForSelect(Class<?> clazz) {
    List<String> listAttribute = listAttributeName(clazz);
    if (listAttribute == null || listAttribute.isEmpty()) {
      return "";
    }
    // Covert CamelCase to SnakeCase
    String attributeNames = listAttribute
      .stream()
      .map(StringUtils::camelCaseToSnakeCase)
      .collect(Collectors.joining(","));
    return attributeNames;
  }
}
