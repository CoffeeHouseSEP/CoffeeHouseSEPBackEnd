package com.sep.coffeemanagement.service.category;

import com.sep.coffeemanagement.dto.category.CategoryReq;
import com.sep.coffeemanagement.dto.category.CategoryRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {
  Optional<CategoryRes> getCategory(String field, String value);

  Optional<ListWrapperResponse<CategoryRes>> getListCategory(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createCategory(CategoryReq req);

  void updateCategory(CategoryReq req);
}
