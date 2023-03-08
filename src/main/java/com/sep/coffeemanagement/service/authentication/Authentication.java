package com.sep.coffeemanagement.service.authentication;

import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginRes;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import java.util.Optional;

public interface Authentication {
  Optional<InternalUserLoginRes> login(InternalUserLoginReq internalUserLoginReq);
  void logout(String id);
}
