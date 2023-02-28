package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.exception.UnauthorizedException;
import com.sep.coffeemanagement.jwt.JwtValidation;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractController<s> {
  @Autowired
  protected s service;

  @Autowired
  protected JwtValidation jwtValidation;

  protected <T> ResponseEntity<CommonResponse<T>> response(
    Optional<T> response,
    String successMessage
  ) {
    return new ResponseEntity<>(
      new CommonResponse<>(true, response.get(), successMessage, HttpStatus.OK.value()),
      HttpStatus.OK
    );
  }

  protected void checkAuthentication(HttpServletRequest request) {
    String token = jwtValidation.getJwtFromRequest(request);
    if (token == null) {
      throw new UnauthorizedException("unauthorized");
    }
  }
}
