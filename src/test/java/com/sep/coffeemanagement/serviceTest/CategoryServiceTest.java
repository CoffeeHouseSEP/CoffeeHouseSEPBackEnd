//package com.sep.coffeemanagement.serviceTest;
//
//import com.sep.coffeemanagement.dto.category.CategoryReq;
//import com.sep.coffeemanagement.dto.category.CategoryRes;
//import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
//import com.sep.coffeemanagement.exception.InvalidRequestException;
//import com.sep.coffeemanagement.exception.ResourceNotFoundException;
//import com.sep.coffeemanagement.service.category.CategoryService;
//import java.util.HashMap;
//import java.util.Optional;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.transaction.annotation.Transactional;
//
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@SpringBootTest
//@Transactional
//public class CategoryServiceTest {
//  @Autowired
//  private CategoryService categoryService;
//
//  @Test
//  @DisplayName("getListCategory: UTC01")
//  public void testGetListCategoryUTC01() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("status", "1");
//    Optional<ListWrapperResponse<CategoryRes>> actual = categoryService.getListCategory(
//      allParams,
//      "",
//      1,
//      10,
//      ""
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(4, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("getListCategory: UTC02")
//  public void testGetListCategoryUTC02() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("statuss", "1");
//    Optional<ListWrapperResponse<CategoryRes>> actual = categoryService.getListCategory(
//      allParams,
//      "",
//      1,
//      10,
//      ""
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(6, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC01 (name blank)")
//  public void testCreateCategoryUTC01() {
//    CategoryReq req = new CategoryReq();
//    req.setName("");
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.createCategory(req);
//      }
//    );
//    String expected = "Tên không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC02 (name over length 100)")
//  public void testCreateCategoryUTC02() {
//    CategoryReq req = new CategoryReq();
//    String name = RandomStringUtils.randomAlphabetic(101).toUpperCase();
//    req.setName(name);
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.createCategory(req);
//      }
//    );
//    String expected = "Tên không được vượt quá 100 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC03 (name duplicate)")
//  public void testCreateCategoryUTC03() {
//    CategoryReq req = new CategoryReq();
//    req.setName("CÀ PHÊ");
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.createCategory(req);
//      }
//    );
//    String expected = "Trùng tên danh mục";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC04 (description blank)")
//  public void testCreateCategoryUTC04() {
//    CategoryReq req = new CategoryReq();
//    String name = RandomStringUtils.randomAlphabetic(5).toUpperCase();
//    req.setName(name);
//    req.setDescription("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.createCategory(req);
//      }
//    );
//    String expected = "Thông tin không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC05 (description over length 4000)")
//  public void testCreateCategoryUTC05() {
//    CategoryReq req = new CategoryReq();
//    String description = RandomStringUtils.randomAlphabetic(4001).toUpperCase();
//    req.setName("utc05");
//    req.setDescription(description);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.createCategory(req);
//      }
//    );
//    String expected = "Thông tin không được vượt quá 4000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("createCategory: UTC06")
//  public void testCreateCategoryUTC06() {
//    CategoryReq req = new CategoryReq();
//    req.setName("anhpd35 test");
//    req.setDescription("anhpd35 test");
//    categoryService.createCategory(req);
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC01 (not found categoryId)")
//  public void testUpdateCategoryUTC01() {
//    CategoryReq req = new CategoryReq();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdcabcde");
//    req.setName("anhpd35 test");
//    req.setDescription("anhpd35 test");
//    ResourceNotFoundException exception = Assert.assertThrows(
//      ResourceNotFoundException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Không tìm thấy danh mục";
//    Assertions.assertEquals(expected, exception.getMessage());
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC02 (name blank)")
//  public void testUpdateCategoryUTC02() {
//    CategoryReq req = new CategoryReq();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName("");
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Tên không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC03 (name over length 100)")
//  public void testUpdateCategoryUTC03() {
//    CategoryReq req = new CategoryReq();
//    String name = RandomStringUtils.randomAlphabetic(101).toUpperCase();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName(name);
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Tên không được vượt quá 100 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC04 (name duplicate)")
//  public void testUpdateCategoryUTC04() {
//    CategoryReq req = new CategoryReq();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName("CÀ PHÊ");
//    req.setDescription("anhpd35 test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Trùng tên danh mục";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC05 (description blank)")
//  public void testUpdateCategoryUTC05() {
//    CategoryReq req = new CategoryReq();
//    String name = RandomStringUtils.randomAlphabetic(5).toUpperCase();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName(name);
//    req.setDescription("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Thông tin không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC06 (description over length 4000)")
//  public void testUpdateCategoryUTC06() {
//    CategoryReq req = new CategoryReq();
//    String description = RandomStringUtils.randomAlphabetic(4001).toUpperCase();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName("utc05");
//    req.setDescription(description);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        categoryService.updateCategory(req);
//      }
//    );
//    String expected = "Thông tin không được vượt quá 4000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("updateCategory: UTC07")
//  public void testUpdateCategoryUTC07() {
//    CategoryReq req = new CategoryReq();
//    req.setCategoryId("ee77010b-2ce5-4baa-a0c5-da1b135b1cdc");
//    req.setName("anhpd35 test");
//    req.setDescription("anhpd35 test");
//    categoryService.updateCategory(req);
//  }
//}
