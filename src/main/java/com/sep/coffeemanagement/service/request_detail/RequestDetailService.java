package com.sep.coffeemanagement.service.request_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request_detail.RequestDetailRes;
import java.util.Optional;

public interface RequestDetailService {
  Optional<ListWrapperResponse<RequestDetailRes>> getListRequestDetailByRequestId(
    String requestId
  );
}
