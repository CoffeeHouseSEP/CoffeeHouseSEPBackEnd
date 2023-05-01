package com.sep.coffeemanagement.service.request;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request.RequestReq;
import com.sep.coffeemanagement.dto.request.RequestRes;
import java.util.Map;
import java.util.Optional;

public interface RequestService {
  Optional<ListWrapperResponse<RequestRes>> getListRequest(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField,
    boolean isBranchRole
  );

  void createRequest(RequestReq req);

  void updateRequest(RequestReq req);

  void changeStatusRequest(RequestReq req, Constant.REQUEST_STATUS status);
}
