package com.sep.coffeemanagement.service.internal_user;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.dto.internal_user_register.InternalUserRegisterReq;
import com.sep.coffeemanagement.dto.mail.DataMailDto;
import com.sep.coffeemanagement.exception.BadSqlException;
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
import java.util.*;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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

    public void updateUser(InternalUserReq user, String id) {
        InternalUser userSave = repository
                .getOneByAttribute("internalUserId", id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        validate(user);
        userSave.setLoginName(user.getLoginName());
        userSave.setPhoneNumber(user.getPhoneNumber());
        repository.insertAndUpdate(userSave, true);
    }

    @Override
    public void updateProfile(InternalUserReq userReq, String id) {
        InternalUser userSave = repository
                .getOneByAttribute("internalUserId", id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        validate(userReq);
        userSave.setEmail(userReq.getEmail());
        userSave.setAddress(userReq.getAddress());
        userSave.setPhoneNumber(userReq.getPhoneNumber());
        repository.insertAndUpdate(userSave, true);
    }

    @Override
    public String register(InternalUserRegisterReq user) {
        //CREATE USER
        validate(user);
        if (checkExistUser(user.getRegisterName())) throw new InvalidRequestException(
                new HashMap<String, String>(),
                "register name is existed!"
        );
        InternalUser userSave = new InternalUser()
                .builder()
                .internalUserId(UUID.randomUUID().toString())
                .loginName(user.getRegisterName())
                .encrPassword(bCryptPasswordEncoder().encode(Constant.DEFAULT_PASSWORD))
                .createdDate(DateFormat.getCurrentTime())
                .email(user.getEmail())
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
    public void forgotPassword(String username) {
        //update password to default
        if (!checkExistUser(username)) throw new InvalidRequestException(
                new HashMap<String, String>(),
                "user name is not existed!"
        );
        InternalUser internalUser = repository
                .getOneByAttribute("loginName", username)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        internalUser.setEncrPassword(
                bCryptPasswordEncoder().encode(Constant.DEFAULT_PASSWORD)
        );
        repository.insertAndUpdate(internalUser, true);
        //SEND MAIL
        try {
            String autoGenPass = mailSenderUtil.autoGeneratePassword();
            Map<String, Object> props = new HashMap<>();
            props.put("name", internalUser.getLoginName());
            props.put("username", internalUser.getLoginName());
            props.put("password", autoGenPass);
            System.out.println(autoGenPass);
            DataMailDto dataMailDto = new DataMailDto()
                    .builder()
                    .to(internalUser.getEmail())
                    .subject(Constant.CLIENT_REGISTER)
                    .props(props)
                    .build();
            mailSenderUtil.sendHtmlMail(dataMailDto, Constant.FORGOT);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePassword(HttpServletRequest http, String newPass) {
        String userId = jwtValidation.getUserIdFromJwt(jwtValidation.getJwtFromRequest(http));
        Optional<InternalUser> internalUser = repository.getOneByAttribute(
                "internalUserId",
                userId
        );
        if (internalUser.isEmpty()) throw new InvalidRequestException(
                new HashMap<>(),
                "user is not exist"
        );
        if (!newPass.matches(TypeValidation.PASSWORD)) throw new InvalidRequestException(
                new HashMap<>(),
                "password does not meet requirements"
        );
        InternalUser userUpdate = internalUser.get();
        userUpdate.setEncrPassword(bCryptPasswordEncoder().encode(newPass));
        repository.insertAndUpdate(userUpdate, true);
    }

    public boolean checkExistUser(String userName) {
        Optional<InternalUser> internalUser = repository.getOneByAttribute(
                "loginName",
                userName
        );
        return internalUser.isPresent();
    }
}
