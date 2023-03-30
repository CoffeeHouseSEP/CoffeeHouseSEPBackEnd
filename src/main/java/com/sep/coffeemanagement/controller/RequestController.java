package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.request.RequestReq;
import com.sep.coffeemanagement.dto.request.RequestRes;
import com.sep.coffeemanagement.service.request.RequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "request")
public class RequestController extends AbstractController<RequestService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-request")
  public ResponseEntity<CommonResponse<String>> addNewRequest(
    @RequestBody RequestReq requestRequest,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    requestRequest.setCreatedBy(userId);
    service.createRequest(requestRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "create request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-request")
  public ResponseEntity<CommonResponse<String>> updateRequest(
    @RequestBody RequestReq requestRequest,
    HttpServletRequest request
  ) {
    service.updateRequest(requestRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "update request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-request")
  public ResponseEntity<CommonResponse<ListWrapperResponse<RequestRes>>> getListRequest(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "0") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(defaultValue = "asc") String keySort,
    @RequestParam(defaultValue = "modified") String sortField,
    HttpServletRequest request
  ) {
    return response(
      service.getListRequest(allParams, keySort, page, pageSize, ""),
      "success"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "cancel-request")
  public ResponseEntity<CommonResponse<String>> cancelRequest(
    @RequestParam String requestId,
    @RequestParam String cancelReason,
    HttpServletRequest request
  ) {
    RequestReq requestRequest = new RequestReq();
    requestRequest.setRequestId(requestId);
    requestRequest.setReason(cancelReason);
    service.changeStatusRequest(requestRequest, Constant.REQUEST_STATUS.CANCELLED);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "cancel request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "send-request")
  public ResponseEntity<CommonResponse<String>> sendRequest(
    @RequestParam String requestId,
    HttpServletRequest request
  ) {
    RequestReq requestRequest = new RequestReq();
    requestRequest.setRequestId(requestId);
    service.changeStatusRequest(requestRequest, Constant.REQUEST_STATUS.PENDING);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "send request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "approve-request")
  public ResponseEntity<CommonResponse<String>> approveRequest(
    @RequestParam String requestId,
    HttpServletRequest request
  ) {
    String userId = checkAuthentication(request);
    RequestReq requestRequest = new RequestReq();
    requestRequest.setRequestId(requestId);
    requestRequest.setApprovedBy(userId);
    service.changeStatusRequest(requestRequest, Constant.REQUEST_STATUS.APPROVED);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "approve request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "complete-request")
  public ResponseEntity<CommonResponse<String>> completeRequest(
    @RequestParam String requestId,
    HttpServletRequest request
  ) {
    RequestReq requestRequest = new RequestReq();
    requestRequest.setRequestId(requestId);
    service.changeStatusRequest(requestRequest, Constant.REQUEST_STATUS.COMPLETED);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "complete request success",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
