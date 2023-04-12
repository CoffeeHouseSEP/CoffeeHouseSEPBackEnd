package com.sep.coffeemanagement.service.request;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request.RequestReq;
import com.sep.coffeemanagement.dto.request.RequestRes;
import com.sep.coffeemanagement.dto.request_detail.RequestDetailReq;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.repository.request.Request;
import com.sep.coffeemanagement.repository.request.RequestRepository;
import com.sep.coffeemanagement.repository.request_detail.RequestDetail;
import com.sep.coffeemanagement.repository.request_detail.RequestDetailRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl
  extends AbstractService<RequestRepository>
  implements RequestService {
  @Autowired
  private GoodsRepository goodsRepository;

  @Autowired
  private RequestDetailRepository requestDetailRepository;

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public Optional<ListWrapperResponse<RequestRes>> getListRequest(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<RequestRes> list = repository.getListRequest(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            request ->
              new RequestRes(
                request.getRequestId(),
                request.getBranchId(),
                request.getCreatedBy(),
                request.getCreatedDate(),
                request.getStatus(),
                request.getApprovedBy(),
                request.getApprovedDate(),
                request.getCompletedDate(),
                request.getCancelledDate(),
                request.getReason(),
                request.getTotalPrice(),
                request.getBranchName(),
                request.getCreatedByName(),
                request.getApprovedByName()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createRequest(RequestReq req) {
    double requestTotalPrice = 0;
    checkValidRequestRequest(req);
    String newId = UUID.randomUUID().toString();
    for (RequestDetailReq requestDetailReq : req.getListRequestDetail()) {
      validate(requestDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", requestDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
      requestTotalPrice += goods.getInnerPrice() * requestDetailReq.getQuantity();

      RequestDetail saveRequestDetail = new RequestDetail();
      saveRequestDetail.setRequestId(newId);
      saveRequestDetail.setRequestDetailId(UUID.randomUUID().toString());
      saveRequestDetail.setQuantity(requestDetailReq.getQuantity());
      saveRequestDetail.setGoodsId(requestDetailReq.getGoodsId());
      saveRequestDetail.setApplyPrice(goods.getInnerPrice());
      requestDetailRepository.insertAndUpdate(saveRequestDetail, false);
    }
    Request requestSave = new Request();
    requestSave.setRequestId(newId);
    requestSave.setBranchId(req.getBranchId());
    requestSave.setCreatedBy(req.getCreatedBy());
    requestSave.setCreatedDate(DateFormat.getCurrentTime());
    requestSave.setStatus(Constant.REQUEST_STATUS.CREATED.toString());
    requestSave.setTotalPrice(requestTotalPrice);
    repository.insertAndUpdate(requestSave, false);
  }

  @Override
  public void updateRequest(RequestReq req) {
    Map<String, String> errors = generateError(RequestReq.class);
    double requestTotalPrice = 0;
    checkValidRequestRequest(req);
    Request request = repository
      .getOneByAttribute("requestId", req.getRequestId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    if (Constant.REQUEST_STATUS.CREATED.toString().equals(req.getStatus())) {
      errors.put("status", "request not in status : CREATED");
      throw new InvalidRequestException(errors, "request not in status : CREATED");
    }
    //clear detail
    requestDetailRepository.removeRequestDetailByRequestId(req.getRequestId());
    //
    for (RequestDetailReq requestDetailReq : req.getListRequestDetail()) {
      validate(requestDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", requestDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
      requestTotalPrice += goods.getInnerPrice() * requestDetailReq.getQuantity();

      RequestDetail saveRequestDetail = new RequestDetail();
      saveRequestDetail.setRequestId(req.getRequestId());
      saveRequestDetail.setRequestDetailId(UUID.randomUUID().toString());
      saveRequestDetail.setQuantity(requestDetailReq.getQuantity());
      saveRequestDetail.setGoodsId(requestDetailReq.getGoodsId());
      saveRequestDetail.setApplyPrice(goods.getInnerPrice());
      requestDetailRepository.insertAndUpdate(saveRequestDetail, false);
    }
    request.setTotalPrice(requestTotalPrice);
    repository.insertAndUpdate(request, true);
  }

  @Override
  public void changeStatusRequest(RequestReq req, Constant.REQUEST_STATUS status) {
    Map<String, String> errors = generateError(RequestReq.class);
    Request request = repository
      .getOneByAttribute("requestId", req.getRequestId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    if (Constant.REQUEST_STATUS.PENDING == status) {
      if (!Constant.REQUEST_STATUS.CREATED.toString().equals(request.getStatus())) {
        errors.put("status", "request not in status CREATED");
        throw new InvalidRequestException(errors, "request not in status CREATED");
      }
      request.setStatus(Constant.REQUEST_STATUS.PENDING.toString());
    } else if (Constant.REQUEST_STATUS.APPROVED == status) {
      if (!Constant.REQUEST_STATUS.PENDING.toString().equals(request.getStatus())) {
        errors.put("status", "request not in status PENDING");
        throw new InvalidRequestException(errors, "request not in status PENDING");
      }
      request.setStatus(Constant.REQUEST_STATUS.APPROVED.toString());
      request.setApprovedBy(req.getApprovedBy());
      request.setApprovedDate(DateFormat.getCurrentTime());
    } else if (Constant.REQUEST_STATUS.COMPLETED == status) {
      if (!Constant.REQUEST_STATUS.APPROVED.toString().equals(request.getStatus())) {
        errors.put("status", "request not in status APPROVED");
        throw new InvalidRequestException(errors, "request not in status APPROVED");
      }
      request.setStatus(Constant.REQUEST_STATUS.COMPLETED.toString());
      request.setCompletedDate(DateFormat.getCurrentTime());
    } else if (Constant.REQUEST_STATUS.CANCELLED == status) {
      if (StringUtils.isNoneEmpty(req.getReason())) {
        request.setStatus(Constant.REQUEST_STATUS.CANCELLED.toString());
        request.setCancelledDate(DateFormat.getCurrentTime());
        request.setReason(req.getReason());
      } else {
        errors.put("reason", "cancel reason is null or empty");
        throw new InvalidRequestException(errors, "cancel reason is null or empty");
      }
    }
    repository.insertAndUpdate(request, true);
  }

  public void checkValidRequestRequest(RequestReq req) {
    Map<String, String> errors = generateError(RequestReq.class);
    validate(req);
    BranchRes branch = branchRepository
      .getBranchByManagerId(req.getCreatedBy())
      .orElseThrow(() -> new ResourceNotFoundException("not branch manager role"));
    req.setBranchId(branch.getBranchId());
    if (req.getListRequestDetail() == null || req.getListRequestDetail().isEmpty()) {
      errors.put("listRequestDetail", "no content");
      throw new InvalidRequestException(errors, "request no content");
    }
  }
}
