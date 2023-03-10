package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.service.internal_user.InternalUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user-internal")
public class InternalUserController extends AbstractController<InternalUserService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-user")
  public ResponseEntity<CommonResponse<String>> addNewUser(
    @RequestBody InternalUserReq userRequest,
    HttpServletRequest request
  ) {
    service.createUser(userRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "create internal user success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-users")
  public ResponseEntity<CommonResponse<ListWrapperResponse<InternalUserRes>>> getListUsers(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListInternalUsers(allParams, keySort, page, pageSize, ""),
      "success"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-user-detail")
  public ResponseEntity<CommonResponse<InternalUserRes>> getUserDetail(
    @RequestParam String field,
    @RequestParam String value
  ) {
    return response(service.getInternalUser(field, value), "success");
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-user")
  public ResponseEntity<CommonResponse<String>> updateUser(
    @RequestBody InternalUserReq userRequest,
    @RequestParam String id,
    HttpServletRequest request
  ) {
    service.updateUser(userRequest, id);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update internal user success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-user-profile")
  public ResponseEntity<CommonResponse<String>> updateUserProfile(
          @RequestBody InternalUserReq userRequest,
          @RequestParam String id,
          HttpServletRequest request
  ) {
    service.updateProfile(userRequest, id);
    return new ResponseEntity<CommonResponse<String>>(
            new CommonResponse<String>(
                    true,
                    null,
                    "update internal user success",
                    HttpStatus.OK.value()
            ),
            null,
            HttpStatus.OK.value()
    );
  }
}
