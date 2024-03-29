package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.news.NewsReq;
import com.sep.coffeemanagement.dto.news.NewsRes;
import com.sep.coffeemanagement.service.news.NewsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "news")
public class NewsController extends AbstractController<NewsService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-news")
  public ResponseEntity<CommonResponse<String>> addNewNews(
    @RequestBody NewsReq newsRequest,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    String userId = checkAuthentication(request);
    newsRequest.setCreatedBy(userId);
    service.createNews(newsRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "Tạo mới thành công", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-news")
  public ResponseEntity<CommonResponse<String>> updateNews(
    @RequestBody NewsReq newsRequest,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.updateNews(newsRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        true,
        null,
        "Cập nhật thành công",
        HttpStatus.OK.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @GetMapping(value = "get-list-news")
  public ResponseEntity<CommonResponse<ListWrapperResponse<NewsRes>>> getListNews(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "0") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    allParams.put("status", "1");
    return response(
      service.getListNews(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-news-authorized")
  public ResponseEntity<CommonResponse<ListWrapperResponse<NewsRes>>> getListNewsAuthorized(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String role = getUserRoleByRequest(request);
    if (!Constant.ADMIN_ROLE.equals(role) && !Constant.BRANCH_ROLE.equals(role)) {
      allParams.put("status", "1");
    }
    return response(
      service.getListNews(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping(value = "remove-news")
  public ResponseEntity<CommonResponse<String>> removeNews(
    @RequestParam String newsId,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.removeNews(newsId);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "Xóa thành công", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }
}
