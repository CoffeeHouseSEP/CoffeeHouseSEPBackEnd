package com.sep.coffeemanagement.service.internal_user;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_profile.InternalUserProfileRes;
import com.sep.coffeemanagement.dto.internal_user_register.InternalUserRegisterReq;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import io.opencensus.common.Internal;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

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

  void updateUser(InternalUserReq user, String id);

  void updateProfile(InternalUserReq userReq, String id);

  String register(InternalUserRegisterReq user);

  void forgotPassword(String username, String email);

  void changePassword(String id, String newPass);

  InternalUserProfileRes getUserProfileById(String id);

  void updateStatus(String id, int status);
}
