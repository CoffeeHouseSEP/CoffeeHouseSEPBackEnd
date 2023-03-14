package com.sep.coffeemanagement.repository.category;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository extends BaseRepository<Category> {
  public String[] categoryIgnores = { "" };

  CategoryRepository() {
    this.ignores = categoryIgnores;
    this.idField = "categoryId";
  }
}
