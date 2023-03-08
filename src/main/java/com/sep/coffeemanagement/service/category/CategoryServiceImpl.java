package com.sep.coffeemanagement.service.category;

import com.sep.coffeemanagement.dto.category.CategoryReq;
import com.sep.coffeemanagement.dto.category.CategoryRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.category.Category;
import com.sep.coffeemanagement.repository.category.CategoryRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl
  extends AbstractService<CategoryRepository>
  implements CategoryService {

  @Override
  public Optional<CategoryRes> getCategory(String field, String value) {
    Category category = repository
      .getOneByAttribute(field, value)
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    return Optional.of(
      new CategoryRes(
        category.getCategoryId(),
        category.getName(),
        category.getDescription(),
        category.getStatus()
      )
    );
  }

  @Override
  public Optional<ListWrapperResponse<CategoryRes>> getListCategory(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<Category> list = repository
      .getListOrEntity(allParams, keySort, page, pageSize, sortField)
      .get();
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            category ->
              new CategoryRes(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                category.getStatus()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createCategory(CategoryReq req) {
    validate(req);
    Category category = objectMapper.convertValue(req, Category.class);
    category.setCategoryId(0);
    category.setStatus(0);
    repository.insertAndUpdate(category, false);
  }

  @Override
  public void updateCategory(CategoryReq req) {
    Category category = repository
      .getOneByAttribute("categoryId", Integer.toString(req.getCategoryId()))
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(req);
    repository.insertAndUpdate(category, true);
  }
}
