package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.custom_anotation.AuthorizationAnnotation;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.dto.internal_user_profile.InternalUserProfileRes;
import com.sep.coffeemanagement.dto.internal_user_register.InternalUserRegisterReq;
import com.sep.coffeemanagement.service.internal_user.InternalUserService;
import com.sep.coffeemanagement.utils.AuthorizationUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.createUser(userRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Tạo mới người dùng thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "active-user")
  public ResponseEntity<CommonResponse<String>> activeUser(
    @RequestParam String id,
    //    @AuthorizationAnnotation(ROLES = {Constant.ADMIN_ROLE})
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.updateStatus(id, 1);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Kích hoạt người dùng thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "de-active-user")
  public ResponseEntity<CommonResponse<String>> deActiveUser(
    @RequestParam String id,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.updateStatus(id, 0);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Vô hiệu hóa người dùng thành công",
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
    @RequestParam(required = false) String keySort,
    @RequestParam(required = false) String sortField,
    HttpServletRequest request
  ) {
    validateAuthorize(
      request,
      new String[] { Constant.ADMIN_ROLE, Constant.BRANCH_ROLE }
    );
    return response(
      service.getListInternalUsers(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-user-detail")
  public ResponseEntity<CommonResponse<InternalUserRes>> getUserDetail(
    @RequestParam String field,
    @RequestParam String value
  ) {
    return response(service.getInternalUser(field, value), "Thành công");
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-user-profile")
  public ResponseEntity<CommonResponse<InternalUserProfileRes>> getUserProfile(
    HttpServletRequest request
  ) {
    String id = checkAuthentication(request);
    return response(Optional.of(service.getUserProfileById(id)), "Thành công");
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-user")
  public ResponseEntity<CommonResponse<String>> updateUser(
    @RequestBody InternalUserReq userRequest,
    HttpServletRequest request
  ) {
    String id = checkAuthentication(request);
    service.updateUser(userRequest, id);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Cập nhật người dùng thành công",
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
    HttpServletRequest request
  ) {
    String id = checkAuthentication(request);
    service.updateProfile(userRequest, id);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Cập nhật thông tin người dùng thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @PostMapping(value = "register")
  public ResponseEntity<CommonResponse<String>> register(
    @RequestBody InternalUserRegisterReq userRequest,
    HttpServletRequest request
  ) {
    service.register(userRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "Đăng ký thành công", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "forgot-password")
  public ResponseEntity<CommonResponse<String>> forgotPassword(
    @RequestParam String username,
    @RequestParam String email,
    HttpServletRequest request
  ) {
    service.forgotPassword(username, email);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Mật khẩu đã được gửi về hòm thư của bạn, vui lòng kiểm tra!!",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "forgot-password-for-admin-bm")
  public ResponseEntity<CommonResponse<String>> forgotPasswordForAdminAndBM(
    @RequestParam String username,
    @RequestParam String email,
    HttpServletRequest request
  ) {
    service.forgotPasswordForAdminAndBM(username, email);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Mật khẩu đã được gửi về hòm thư của bạn, vui lòng kiểm tra!!",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "change-password")
  public ResponseEntity<CommonResponse<String>> changePassword(
    @RequestParam String newPass,
    @RequestParam String oldPass,
    @RequestParam String rePass,
    HttpServletRequest request
  ) {
    validateAuthorize(
      request,
      new String[] { Constant.BRANCH_ROLE, Constant.ADMIN_ROLE, Constant.USER_ROLE }
    );
    service.changePassword(checkAuthentication(request), newPass, oldPass, rePass);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Đổi password thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
