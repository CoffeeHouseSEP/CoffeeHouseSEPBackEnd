package com.sep.coffeemanagement.utils;

import com.sep.coffeemanagement.controller.AbstractController;
import com.sep.coffeemanagement.exception.ForbiddenException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {
  @Autowired
  private UserRepository userRepository;

  public void checkAuthorize(String id, String[] roles) {
    InternalUser user = userRepository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    if (!Arrays.asList(roles).contains(user.getRole())) {
      throw new ForbiddenException("Forbidden Role!");
    }
  }

  public String getUserRoleByUserId(String userId) {
    InternalUser user = userRepository
      .getOneByAttribute("internalUserId", userId)
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    return user.getRole();
  }
}
