package com.sep.coffeemanagement.service.authentication;

import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.jwt.JwtValidation;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.nio.charset.Charset;
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
    InternalUser user = checkUsername(internalUserLoginReq.getLoginName())
      .orElseThrow(
        () -> {
          error.put("loginName", "not found username!");
          return new InvalidRequestException(error, "Username is not found!");
        }
      );

    String decodedPassword = new String(
      Base64.decodeBase64(internalUserLoginReq.getLoginPassword()),
      StandardCharsets.UTF_8
    );
    System.out.println(decodedPassword);
    if (!bCryptPasswordEncoder().matches(decodedPassword, user.getEncrPassword())) {
      error.put("loginPassword", "is wrong!");
      throw new InvalidRequestException(error, "loginPassword is wrong");
    }
    String token = jwtValidation.generateToken(String.valueOf(user.getInternalUserId()));
    user.setToken(token);
    repository.insertAndUpdate(user, true);
    return Optional.of(new InternalUserLoginRes(token));
  }

  @Override
  public void logout(String id) {
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new NotFoundException("user not found"));
    internalUser.setToken("");
    repository.insertAndUpdate(internalUser, true);
  }

  public Optional<InternalUser> checkUsername(String username) {
    //        if(username.matches(TypeValidation.EMAIL)) return repository.getOneByAttribute("email", username);
    if (username.matches(TypeValidation.PHONE)) return repository.getOneByAttribute(
      "phoneNumber",
      username
    );
    return repository.getOneByAttribute("loginName", username);
  }
}
