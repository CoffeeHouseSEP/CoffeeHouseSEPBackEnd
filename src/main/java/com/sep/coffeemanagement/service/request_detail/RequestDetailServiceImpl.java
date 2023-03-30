package com.sep.coffeemanagement.service.request_detail;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request_detail.RequestDetailRes;
import com.sep.coffeemanagement.repository.request_detail.RequestDetailRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RequestDetailServiceImpl
  extends AbstractService<RequestDetailRepository>
  implements RequestDetailService {

  @Override
  public Optional<ListWrapperResponse<RequestDetailRes>> getListRequestDetailByRequestId(
    String requestId
  ) {
    List<RequestDetailRes> list = repository.getListRequestDetailByRequestId(requestId);
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            requestDetail ->
              new RequestDetailRes(
                requestDetail.getRequestDetailId(),
                requestDetail.getRequestId(),
                requestDetail.getGoodsId(),
                requestDetail.getQuantity(),
                requestDetail.getApplyPrice(),
                requestDetail.getGoodsName()
              )
          )
          .collect(Collectors.toList()),
        0,
        0,
        list.size()
      )
    );
  }
}
