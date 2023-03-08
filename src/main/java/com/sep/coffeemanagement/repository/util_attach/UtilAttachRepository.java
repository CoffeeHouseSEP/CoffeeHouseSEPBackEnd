package com.sep.coffeemanagement.repository.util_attach;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UtilAttachRepository extends BaseRepository<UtilAttach> {
  public String[] utilAttachIgnores = { "utilAttachId" };

  UtilAttachRepository() {
    this.ignores = utilAttachIgnores;
    this.idField = "utilAttachId";
  }
}
