package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.constant.Constant;
import com.sep.coffeemanagement.dto.category.CategoryReq;
import com.sep.coffeemanagement.dto.category.CategoryRes;
import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.service.category.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "category")
public class CategoryController extends AbstractController<CategoryService> {

  @SecurityRequirement(name = "Bearer Authentication")
  @PostMapping(value = "add-new-category")
  public ResponseEntity<CommonResponse<String>> addNewCategory(
    @RequestBody CategoryReq categoryRequest,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.createCategory(categoryRequest);
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(true, null, "Tạo mới thành công", HttpStatus.OK.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @PutMapping(value = "update-category")
  public ResponseEntity<CommonResponse<String>> updateCategory(
    @RequestBody CategoryReq categoryRequest,
    HttpServletRequest request
  ) {
    validateAuthorize(request, new String[] { Constant.ADMIN_ROLE });
    service.updateCategory(categoryRequest);
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

  @GetMapping(value = "get-list-category")
  public ResponseEntity<CommonResponse<ListWrapperResponse<CategoryRes>>> getListCategory(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    allParams.put("status", "1");
    return response(
      service.getListCategory(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }

  @SecurityRequirement(name = "Bearer Authentication")
  @GetMapping(value = "get-list-category-authorized")
  public ResponseEntity<CommonResponse<ListWrapperResponse<CategoryRes>>> getListCategoryAuthorized(
    @RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false, defaultValue = "10") int pageSize,
    @RequestParam Map<String, String> allParams,
    @RequestParam(required = false, defaultValue = "") String keySort,
    @RequestParam(required = false, defaultValue = "") String sortField,
    HttpServletRequest request
  ) {
    String role = getUserRoleByRequest(request);
    if (!Constant.ADMIN_ROLE.equals(role)) {
      allParams.put("status", "1");
    }
    return response(
      service.getListCategory(allParams, keySort, page, pageSize, sortField),
      "Thành công"
    );
  }
}
