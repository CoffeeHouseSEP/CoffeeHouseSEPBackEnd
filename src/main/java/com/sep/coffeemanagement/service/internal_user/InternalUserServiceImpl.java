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
        user.getStatus()
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
                user.getStatus()
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
    userSave.setStatus(0);
    repository.insertAndUpdate(userSave, false);
  }

  public void updateUser(InternalUserReq user) {
    if (!checkExistUser(user.getLoginName())) throw new ResourceNotFoundException(
      "username not found!"
    );
    InternalUser userSave = repository
      .getOneByAttribute("loginName", user.getLoginName())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(user);
    userSave.setLoginName(user.getLoginName());
    userSave.setPhoneNumber(user.getPhoneNumber());
    userSave.setAddress(user.getAddress());
    repository.insertAndUpdate(userSave, true);
  }

  @Override
  public void updateProfile(InternalUserReq userReq, String id) {
    InternalUser userSave = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(userReq);
    if (checkExistUser(userReq.getLoginName())) throw new InvalidRequestException(
      new HashMap<String, String>(),
      "this username is existed!!"
    );
    userSave.setLoginName(userReq.getLoginName());
    userSave.setAddress(userReq.getAddress());
    userSave.setPhoneNumber(userReq.getPhoneNumber());
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
      .build();
    repository.insertAndUpdate(userSave, false);
    //SEND MAIL
    try {
      Map<String, Object> props = new HashMap<>();
      props.put("name", user.getRegisterName());
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
    if (!checkExistUser(username)) throw new InvalidRequestException(
      new HashMap<String, String>(),
      "user name is not existed!"
    );
    String autoGenPass = mailSenderUtil.autoGeneratePassword();
    InternalUser internalUser = repository
      .getOneByAttribute("loginName", username)
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    if (!email.matches(TypeValidation.EMAIL)) throw new InvalidRequestException(
      new HashMap<String, String>(),
      "email is not well-formed!"
    );
    if (!email.equals(internalUser.getEmail())) throw new InvalidRequestException(
      new HashMap<String, String>(),
      "email is not correct!"
    ); //if the input email is not belong to the username then reject
    String hashedPass = bCryptPasswordEncoder().encode(autoGenPass);
    System.out.println(hashedPass);
    internalUser.setEncrPassword(hashedPass);
    repository.insertAndUpdate(internalUser, true);
    //SEND MAIL
    try {
      Map<String, Object> props = new HashMap<>();
      props.put("name", internalUser.getLoginName());
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
    if (
      !repository.checkDuplicateFieldValue("internalUserId", id, null)
    ) throw new InvalidRequestException(new HashMap<>(), "user is not exist");
    if (newPass == null | newPass.length() == 0) throw new InvalidRequestException(
      new HashMap<>(),
      "password does not allowed to be null or empty"
    );
    InternalUser userUpdate = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow();
    userUpdate.setEncrPassword(bCryptPasswordEncoder().encode(newPass));
    repository.insertAndUpdate(userUpdate, true);
  }

  @Override
  public InternalUserProfileRes getUserProfileById(String id) {
    InternalUser internalUser = repository
      .getOneByAttribute("internalUserId", id)
      .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    return objectMapper.convertValue(internalUser, InternalUserProfileRes.class);
  }

  public boolean checkExistUser(String userName) {
    return repository.checkDuplicateFieldValue("login_name", userName, null);
  }
}
