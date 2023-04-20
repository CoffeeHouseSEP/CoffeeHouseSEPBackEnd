package com.sep.coffeemanagement.service.internal_user;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.dto.internal_user_profile.InternalUserProfileRes;
import com.sep.coffeemanagement.dto.internal_user_register.InternalUserRegisterReq;
import com.sep.coffeemanagement.dto.mail.DataMailDto;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.jwt.JwtValidation;
import com.sep.coffeemanagement.log.AppLogger;
import com.sep.coffeemanagement.log.LoggerFactory;
import com.sep.coffeemanagement.log.LoggerType;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import com.sep.coffeemanagement.utils.MailSenderUtil;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalUserServiceImpl
  extends AbstractService<UserRepository>
  implements InternalUserService {
  protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

  @Autowired
  private MailSenderUtil mailSenderUtil;

  @Autowired
  private JwtValidation jwtValidation;

  @Override
  public Optional<InternalUserRes> getInternalUser(String field, String value) {
    InternalUser user = repository
      .getOneByAttribute(field.trim(), value.trim())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    return Optional.of(
      new InternalUserRes(
        user.getInternalUserId(),
        user.getLoginName(),
        user.getPhoneNumber(),
        DateFormat.toDateString(user.getCreatedDate(), DateTime.YYYY_MM_DD),
        user.getEmail(),
        user.getAddress(),
        user.getStatus(),
        user.getRole(),
        user.getFullName()
      )
    );
  }

  public Optional<ListWrapperResponse<InternalUserRes>> getListInternalUsers(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<InternalUser> list = repository
      .getListOrEntity(allParams, keySort, page, pageSize, sortField)
      .get();
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            user ->
              new InternalUserRes(
                user.getInternalUserId(),
                user.getLoginName(),
                user.getPhoneNumber(),
                DateFormat.toDateString(user.getCreatedDate(), DateTime.YYYY_MM_DD),
                user.getEmail(),
                user.getAddress(),
                user.getStatus(),
                user.getRole(),
                user.getFullName()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  public void createUser(InternalUserReq user) {
    validate(user);
    Map<String, String> er = generateError(InternalUserReq.class);
    if (checkExistUser(user.getLoginName().trim())) {
      er.put("loginName", "Tên đăng nhập đã tồn tại!");
      throw new InvalidRequestException(er, "Tên đăng nhập đã tồn tại!");
    }
    if (checkExistEmail(user.getEmail().trim())) {
      er.put("email", "Email đã tồn tại!");
      throw new InvalidRequestException(er, "Email đã tồn tại!!");
    }
    InternalUser userSave = objectMapper.convertValue(user, InternalUser.class);
    String newId = UUID.randomUUID().toString();
    userSave.setInternalUserId(newId);
    userSave.setEncrPassword(bCryptPasswordEncoder().encode(Constant.DEFAULT_PASSWORD));
    userSave.setCreatedDate(DateFormat.getCurrentTime());
    userSave.setStatus(1);
    userSave.setRole(Constant.BRANCH_ROLE);
    userSave.setFullName(user.getFullName());
    repository.insertAndUpdate(userSave, false);
  }

  public void updateUser(InternalUserReq user, String id) {
    Map<String, String> er = generateError(InternalUserReq.class);
    if (
      checkExistUserWithExceptId(user.getLoginName(), id)
    ) throw new ResourceNotFoundException("Trùng tên người dùng");
    if (checkExistEmailWithExceptId(user.getEmail().trim(), id)) {
      er.put("email", "Email đã tồn tại!");
      throw new InvalidRequestException(er, "Email đã tồn tại!");
    }
    InternalUser userSave = repository
      .getOneByAttribute("loginName", user.getLoginName())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    validate(user);
    userSave.setLoginName(user.getLoginName());
    userSave.setPhoneNumber(user.getPhoneNumber());
    userSave.setAddress(user.getAddress());
    userSave.setFullName(user.getFullName());
    repository.insertAndUpdate(userSave, true);
  }

  @Override
  public void updateProfile(InternalUserReq userReq, String id) {
    Map<String, String> er = generateError(InternalUserReq.class);
    InternalUser userSave = repository
      .getOneByAttribute("internalUserId", id.trim())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    validate(userReq);
    if (checkExistUserWithExceptId(userReq.getLoginName().trim(), id)) {
      er.put("loginName", "Tên đăng nhập đã tồn tại!");
      throw new InvalidRequestException(er, "Tên đăng nhập đã tồn tại!");
    }
    if (checkExistEmailWithExceptId(userReq.getEmail().trim(), id)) {
      er.put("email", "Email đã tồn tại!");
      throw new InvalidRequestException(er, "Email đã tồn tại!");
    }
    userSave.setLoginName(userReq.getLoginName());
    userSave.setAddress(userReq.getAddress());
    userSave.setPhoneNumber(userReq.getPhoneNumber());
    userSave.setFullName(userReq.getFullName());
    repository.insertAndUpdate(userSave, true);
  }

  @Override
  public String register(InternalUserRegisterReq user) {
    //CREATE USER
    validate(user);
    String rawPass = new String(
      Base64.decodeBase64(user.getRegisterPassword()),
      StandardCharsets.UTF_8
    );
    Map<String, String> errors = generateError(InternalUserRegisterReq.class);
    if (!rawPass.matches(TypeValidation.PASSWORD)) {
      errors.put("registerPassword", "Mật khẩu không đúng định dạng");
      throw new InvalidRequestException(errors, "Mật khẩu không đúng định dạng");
    }
    if (checkExistUser(user.getRegisterName())) {
      errors.put("registerName", "Tên đăng nhập đã tồn tại!");
      throw new InvalidRequestException(errors, "Tên đăng nhập đã tồn tại!");
    }
    if (checkExistEmail(user.getEmail())) {
      errors.put("email", "Email đã tồn tại!");
      throw new InvalidRequestException(errors, "Email đã tồn tại!");
    }
    InternalUser userSave = new InternalUser()
      .builder()
      .internalUserId(UUID.randomUUID().toString())
      .loginName(user.getRegisterName())
      .encrPassword(bCryptPasswordEncoder().encode(rawPass))
      .createdDate(DateFormat.getCurrentTime())
      .email(user.getEmail())
      .role(Constant.USER_ROLE)
      .fullName(user.getFullName())
      .status(1)
      .build();
    repository.insertAndUpdate(userSave, false);
    //SEND MAIL
    try {
      Map<String, Object> props = new HashMap<>();
      props.put("name", user.getFullName());
      props.put("username", user.getRegisterName());
      props.put("password", rawPass);
      DataMailDto dataMailDto = new DataMailDto()
        .builder()
        .to(user.getEmail())
        .subject(Constant.CLIENT_REGISTER)
        .props(props)
        .build();
      mailSenderUtil.sendHtmlMail(dataMailDto, Constant.REGISTER);
      return userSave.getInternalUserId();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void forgotPassword(String username, String email) {
    username = username.trim();
    email = email.trim();
    //update password to default
    Map<String, String> er = new HashMap<>();
    if (!checkExistUser(username)) {
      er.put("username", "Tên đăng nhập không tồn tại");
      throw new InvalidRequestException(er, "Tên đăng nhập không tồn tại");
    }
    String autoGenPass = mailSenderUtil.autoGeneratePassword();
    InternalUser internalUser = repository
      .getOneByAttribute("loginName", username)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    if (!email.matches(TypeValidation.EMAIL)) {
      er.put("email", "Email không đúng định dạng");
      throw new InvalidRequestException(er, "Email không đúng định dạng");
    }
    if (!email.equals(internalUser.getEmail())) {
      er.put("email", "Email không chính xác");
      throw new InvalidRequestException(er, "Email không chính xác");
    } //if the input email is not belong to the username then reject
    String hashedPass = bCryptPasswordEncoder().encode(autoGenPass);
    System.out.println(hashedPass);
    internalUser.setEncrPassword(hashedPass);
    repository.insertAndUpdate(internalUser, true);
    //SEND MAIL
    try {
      Map<String, Object> props = new HashMap<>();
      props.put("name", internalUser.getFullName());
      props.put("username", internalUser.getLoginName());
      props.put("password", autoGenPass);
      System.out.println(autoGenPass);
      DataMailDto dataMailDto = new DataMailDto()
        .builder()
        .to(internalUser.getEmail())
        .subject(Constant.CLIENT_FORGOTPASSWORD)
        .props(props)
        .build();
      mailSenderUtil.sendHtmlMail(dataMailDto, Constant.FORGOT);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void changePassword(String id, String newPass) {
    newPass = newPass.trim();
    Map<String, String> e = new HashMap<>();
    if (StringUtils.isEmpty(id)) {
      e.put("id", "Không tìm thấy!");
      throw new InvalidRequestException(e, "Không tìm thấy người dùng");
    }
    if (newPass == null | newPass.length() == 0) {
      e.put("newPass", "Không được để trống!");
      throw new InvalidRequestException(e, "Mật khẩu không được để trống");
    }
    String decodedPassword = new String(
      Base64.decodeBase64(newPass),
      StandardCharsets.UTF_8
    );
    if (!decodedPassword.matches(TypeValidation.PASSWORD)) {
      Map<String, String> er = generateError(String.class);
      er.put("new pass", "Mật khẩu mới không đúng định dạng");
      throw new InvalidRequestException(er, "Mật khẩu mới không đúng định dạng");
    }
    InternalUser userUpdate = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow();
    userUpdate.setEncrPassword(
      bCryptPasswordEncoder()
        .encode(new String(Base64.decodeBase64(newPass), StandardCharsets.UTF_8))
    );
    repository.insertAndUpdate(userUpdate, true);
  }

  @Override
  public InternalUserProfileRes getUserProfileById(String id) {
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    return objectMapper.convertValue(internalUser, InternalUserProfileRes.class);
  }

  @Override
  public void updateStatus(String id, int status) {
    Boolean checkExist = repository.checkDuplicateFieldValue(
      "internal_user_id",
      id,
      null
    );
    Map<String, String> errors = generateError(String.class);
    if (!checkExist) throw new ResourceNotFoundException("Không tìm thấy người dùng");
    if (status < 0) {
      errors.put("status", "status is not valid!");
      throw new InvalidRequestException(errors, "status is not valid!");
    }
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    internalUser.setStatus(status);
    repository.insertAndUpdate(internalUser, true);
  }

  public boolean checkExistUser(String userName) {
    return repository.checkDuplicateFieldValue("login_name", userName, null);
  }

  public boolean checkExistEmail(String email) {
    return repository.checkDuplicateFieldValue("email", email, null);
  }

  public boolean checkExistUserWithExceptId(String userName, String exceptId) {
    return repository.checkDuplicateFieldValue("login_name", userName, exceptId);
  }

  public boolean checkExistEmailWithExceptId(String email, String exceptId) {
    return repository.checkDuplicateFieldValue("email", email, exceptId);
  }
}
