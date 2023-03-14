package com.sep.coffeemanagement.repository.internal_user;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository<InternalUser> {
  public String[] userIgnores = { "" };

  UserRepository() {
    this.ignores = userIgnores;
    this.idField = "internalUserId";
  }
}
