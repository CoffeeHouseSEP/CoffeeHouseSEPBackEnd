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
      .getOneByAttribute(field, value)
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
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
    if (
      checkExistUserWithExceptId(user.getLoginName(), id)
    ) throw new ResourceNotFoundException("username is duplicate");
    InternalUser userSave = repository
      .getOneByAttribute("loginName", user.getLoginName())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(user);
    userSave.setLoginName(user.getLoginName());
    userSave.setPhoneNumber(user.getPhoneNumber());
    userSave.setAddress(user.getAddress());
    userSave.setFullName(user.getFullName());
    repository.insertAndUpdate(userSave, true);
  }

  @Override
  public void updateProfile(InternalUserReq userReq, String id) {
    Map<String, String> er = generateError(InternalUser.class);
    InternalUser userSave = repository
      .getOneByAttribute("internalUserId", id.trim())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(userReq);
    if (checkExistUserWithExceptId(userReq.getLoginName().trim(), id)) {
      er.put("loginName", "existed!");
      throw new InvalidRequestException(er, "this username is existed!!");
    }
    if (checkExistUserWithExceptId(userReq.getEmail().trim(), id)) {
      er.put("email", "existed!");
      throw new InvalidRequestException(er, "this email is existed!!");
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
      errors.put("registerPassword", "password is not well formed!");
      throw new InvalidRequestException(errors, "register password is in wrong format!");
    }
    if (checkExistUser(user.getRegisterName())) {
      errors.put("registerName", "registerName is existed");
      throw new InvalidRequestException(errors, "register name is existed!");
    }
    if (checkExistUser(user.getEmail())) {
      errors.put("email", "register email is existed");
      throw new InvalidRequestException(errors, "register email is existed!");
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
      props.put("password", user.getRegisterPassword());
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
    //update password to default
    Map<String, String> er = new HashMap<>();
    if (!checkExistUser(username)) {
      er.put("username", "not existed!!");
      throw new InvalidRequestException(er, "user name is not existed!");
    }
    String autoGenPass = mailSenderUtil.autoGeneratePassword();
    InternalUser internalUser = repository
      .getOneByAttribute("loginName", username)
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    if (!email.matches(TypeValidation.EMAIL)) {
      er.put("email", "not well-formatted!");
      throw new InvalidRequestException(er, "email is not well-formed!");
    }
    if (!email.equals(internalUser.getEmail())) {
      er.put("email", "not well-formatted!");
      throw new InvalidRequestException(er, "email is not correct!");
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
    if (StringUtils.isEmpty(id)) {
      throw new InvalidRequestException(
        new HashMap<String, String>(),
        "user id from request is empty!"
      );
    }
    if (newPass == null | newPass.length() == 0) throw new InvalidRequestException(
      new HashMap<>(),
      "password does not allowed to be null or empty"
    );
    String decodedPassword = new String(
      Base64.decodeBase64(newPass),
      StandardCharsets.UTF_8
    );
    if (!decodedPassword.matches(TypeValidation.PASSWORD)) {
      Map<String, String> er = generateError(String.class);
      er.put("new pass", "not well formed!");
      throw new InvalidRequestException(er, "password is not in valid form!");
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
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
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
    if (!checkExist) throw new ResourceNotFoundException("this user id not found!");
    if (status < 0) {
      errors.put("status", "status is not valid!");
      throw new InvalidRequestException(errors, "status is not valid!");
    }
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("user is not existed!"));
    internalUser.setStatus(status);
    repository.insertAndUpdate(internalUser, true);
  }

  public boolean checkExistUser(String userName) {
    return repository.checkDuplicateFieldValue("login_name", userName, null);
  }

  public boolean checkExistUserWithExceptId(String userName, String exceptId) {
    return repository.checkDuplicateFieldValue("login_name", userName, exceptId);
  }
}
