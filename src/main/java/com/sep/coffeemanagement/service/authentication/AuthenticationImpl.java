package com.sep.coffeemanagement.service.authentication;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.jwt.JwtValidation;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class AuthenticationImpl
  extends AbstractService<UserRepository>
  implements Authentication {
  @Autowired
  private JwtValidation jwtValidation;

  @Override
  public Optional<InternalUserLoginRes> login(InternalUserLoginReq internalUserLoginReq) {
    Map<String, String> error = new HashMap<>();
    if (!checkExistUser(internalUserLoginReq.getLoginName().trim())) {
      error.put("loginName", "Không tìm thấy tên đăng nhập");
      throw new InvalidRequestException(error, "Không tìm thấy tên đăng nhập");
    }
    InternalUser user = repository
      .getOneByAttribute("loginName", internalUserLoginReq.getLoginName().trim())
      .orElse(null);
    if (user.getStatus() != 1) {
      error.put("status", "Người dùng đã bị vô hiệu hóa");
      throw new InvalidRequestException(error, "Người dùng đã bị vô hiệu hóa");
    }
    String decodedPassword = new String(
      Base64.decodeBase64(internalUserLoginReq.getLoginPassword()),
      StandardCharsets.UTF_8
    );
    System.out.println(decodedPassword);
    if (!bCryptPasswordEncoder().matches(decodedPassword, user.getEncrPassword())) {
      error.put("loginPassword", "Mật khẩu không chính xác");
      throw new InvalidRequestException(error, "Mật khẩu không chính xác");
    }
    String token = jwtValidation.generateToken(String.valueOf(user.getInternalUserId()));
    user.setToken(token);
    repository.insertAndUpdate(user, true);
    return Optional.of(new InternalUserLoginRes(token, user.getRole()));
  }

  @Override
  public void logout(String id) {
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
    internalUser.setToken("");
    repository.insertAndUpdate(internalUser, true);
  }

  public boolean checkExistUser(String userName) {
    return repository.checkDuplicateFieldValue("login_name", userName, null);
  }

  public boolean checkExistUserWithExceptId(String userName, String exceptId) {
    return repository.checkDuplicateFieldValue("login_name", userName, exceptId);
  }
}
