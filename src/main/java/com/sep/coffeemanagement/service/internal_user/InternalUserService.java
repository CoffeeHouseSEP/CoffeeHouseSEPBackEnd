package com.sep.coffeemanagement.service.internal_user;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import java.util.Map;
import java.util.Optional;

public interface InternalUserService {
  Optional<InternalUserRes> getInternalUser(String field, String value);

  Optional<ListWrapperResponse<InternalUserRes>> getListInternalUsers(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createUser(InternalUserReq user);

  void updateUser(InternalUserReq user, int id);
}
