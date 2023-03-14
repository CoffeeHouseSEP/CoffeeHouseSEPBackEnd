package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginRes;
import com.sep.coffeemanagement.service.authentication.Authentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "authentication")
public class AuthenticationController extends AbstractController<Authentication> {

  @PostMapping(value = "login")
  public ResponseEntity<CommonResponse<InternalUserLoginRes>> login(
    @RequestBody InternalUserLoginReq internalUserLoginReq
  ) {
    return response(service.login(internalUserLoginReq), "success");
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "logout")
  public ResponseEntity<CommonResponse<String>> logout(
    HttpServletRequest httpServletRequest
  ) {
    String id = checkAuthentication(httpServletRequest);
    service.logout(id);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "logout success", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }
}
