package com.sep.coffeemanagement.service.request;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.constant.DateTime;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    String sortField,
    boolean isBranchRole
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
          .filter(
            requestRes ->
              !requestRes
                .getStatus()
                .equals(Constant.REQUEST_STATUS.CREATED.toString()) ||
              isBranchRole
          )
          .map(
            request ->
              new RequestRes(
                request.getRequestId(),
                request.getBranchId(),
                request.getCreatedBy(),
                DateFormat.convertDateStringFormat(
                  request.getCreatedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                request.getStatus(),
                request.getApprovedBy(),
                DateFormat.convertDateStringFormat(
                  request.getApprovedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                DateFormat.convertDateStringFormat(
                  request.getCompletedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                DateFormat.convertDateStringFormat(
                  request.getCancelledDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
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
  @Transactional
  public void createRequest(RequestReq req) {
    double requestTotalPrice = 0;
    checkValidRequestRequest(req, false);
    String newId = UUID.randomUUID().toString();
    for (RequestDetailReq requestDetailReq : req.getListRequestDetail()) {
      validate(requestDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", requestDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
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
  @Transactional
  public void updateRequest(RequestReq req) {
    Map<String, String> errors = generateError(RequestReq.class);
    double requestTotalPrice = 0;
    checkValidRequestRequest(req, true);
    Request request = repository
      .getOneByAttribute("requestId", req.getRequestId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu nhập"));
    if (Constant.REQUEST_STATUS.CREATED.toString().equals(req.getStatus())) {
      errors.put("status", "Yêu cầu nhập không ở trong trạng thái MỚI TẠO");
      throw new InvalidRequestException(
        errors,
        "Yêu cầu nhập không ở trong trạng thái MỚI TẠO"
      );
    }
    //clear detail
    requestDetailRepository.removeRequestDetailByRequestId(req.getRequestId());
    //
    for (RequestDetailReq requestDetailReq : req.getListRequestDetail()) {
      validate(requestDetailReq);
      Goods goods = goodsRepository
        .getOneByAttribute("goodsId", requestDetailReq.getGoodsId())
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
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
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy yêu cầu nhâp"));
    if (Constant.REQUEST_STATUS.PENDING == status) {
      if (!Constant.REQUEST_STATUS.CREATED.toString().equals(request.getStatus())) {
        errors.put("status", "Yêu cầu nhập không ở trong trạng thái MỚI TẠO");
        throw new InvalidRequestException(
          errors,
          "Yêu cầu nhập không ở trong trạng thái MỚI TẠO"
        );
      }
      request.setStatus(Constant.REQUEST_STATUS.PENDING.toString());
    } else if (Constant.REQUEST_STATUS.APPROVED == status) {
      if (!Constant.REQUEST_STATUS.PENDING.toString().equals(request.getStatus())) {
        errors.put("status", "Yêu cầu nhập không ở trong trạng thái ĐANG XỬ LÝ");
        throw new InvalidRequestException(
          errors,
          "Yêu cầu nhập không ở trong trạng thái ĐANG XỬ LÝ"
        );
      }
      request.setStatus(Constant.REQUEST_STATUS.APPROVED.toString());
      request.setApprovedBy(req.getApprovedBy());
      request.setApprovedDate(DateFormat.getCurrentTime());
    } else if (Constant.REQUEST_STATUS.COMPLETED == status) {
      if (!Constant.REQUEST_STATUS.APPROVED.toString().equals(request.getStatus())) {
        errors.put("status", "Yêu cầu nhập không ở trong trạng thái ĐÃ DUYỆT");
        throw new InvalidRequestException(
          errors,
          "Yêu cầu nhập không ở trong trạng thái ĐÃ DUYỆT"
        );
      }
      request.setStatus(Constant.REQUEST_STATUS.COMPLETED.toString());
      request.setCompletedDate(DateFormat.getCurrentTime());
    } else if (Constant.REQUEST_STATUS.CANCELLED == status) {
      if (
        !Constant.REQUEST_STATUS.CREATED.toString().equals(request.getStatus()) &&
        !Constant.REQUEST_STATUS.PENDING.toString().equals(request.getStatus())
      ) {
        errors.put(
          "status",
          "Yêu cầu nhập không ở trong trạng thái MỚI TẠO hoặc ĐANG XỬ LÝ"
        );
        throw new InvalidRequestException(
          errors,
          "Yêu cầu nhập không ở trong trạng thái MỚI TẠO hoặc ĐANG XỬ LÝ"
        );
      }
      if (StringUtils.hasText(req.getReason())) {
        request.setStatus(Constant.REQUEST_STATUS.CANCELLED.toString());
        request.setCancelledDate(DateFormat.getCurrentTime());
        request.setReason(req.getReason());
      } else {
        errors.put("reason", "Lý do hủy không được để trống");
        throw new InvalidRequestException(errors, "Lý do hủy không được để trống");
      }
    }
    repository.insertAndUpdate(request, true);
  }

  public void checkValidRequestRequest(RequestReq req, boolean isUpdate) {
    Map<String, String> errors = generateError(RequestReq.class);
    validate(req);
    BranchRes branch = branchRepository
      .getBranchByManagerId(req.getCreatedBy())
      .orElseThrow(
        () -> new ResourceNotFoundException("Không tìm thấy chi nhánh đối với người dùng")
      );
    req.setBranchId(branch.getBranchId());
    if (req.getListRequestDetail() == null || req.getListRequestDetail().isEmpty()) {
      errors.put("listRequestDetail", "Chi tiết hàng hóa không được để trống");
      throw new InvalidRequestException(errors, "Chi tiết hàng hóa không được để trống");
    }
    Optional<List<Request>> listIncompleteRequest = repository.getListIncompleteRequestInBranch(
      branch.getBranchId(),
      isUpdate ? req.getRequestId() : ""
    );
    if (!listIncompleteRequest.get().isEmpty()) {
      errors.put("branchId", "Chi nhánh có yêu cầu nhập chưa hoàn tất quy trình");
      throw new InvalidRequestException(
        errors,
        "Chi nhánh có yêu cầu nhập chưa hoàn tất quy trình"
      );
    }
  }
}
