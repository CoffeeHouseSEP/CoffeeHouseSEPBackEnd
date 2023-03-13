package com.sep.coffeemanagement.service.internal_user;

import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.internal_user.InternalUserReq;
import com.sep.coffeemanagement.dto.internal_user.InternalUserRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class InternalUserServiceImpl
  extends AbstractService<UserRepository>
  implements InternalUserService {

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
    userSave.setEncrPassword(bCryptPasswordEncoder().encode("Abcd@1234"));
    userSave.setCreatedDate(DateFormat.getCurrentTime());
    userSave.setStatus(0);
    repository.insertAndUpdate(userSave, false);
  }

  public void updateUser(InternalUserReq user, int id) {
    InternalUser userSave = repository
      .getOneByAttribute("internalUserId", Integer.toString(id))
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(user);
    userSave.setLoginName(user.getLoginName());
    userSave.setPhoneNumber(user.getPhoneNumber());
    repository.insertAndUpdate(userSave, true);
  }
}
