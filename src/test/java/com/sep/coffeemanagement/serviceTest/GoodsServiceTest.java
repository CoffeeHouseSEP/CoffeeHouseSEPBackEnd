//package com.sep.coffeemanagement.serviceTest;
//
//import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
//import com.sep.coffeemanagement.dto.goods.GoodsReq;
//import com.sep.coffeemanagement.dto.goods.GoodsRes;
//import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
//import com.sep.coffeemanagement.exception.InvalidRequestException;
//import com.sep.coffeemanagement.exception.ResourceNotFoundException;
//import com.sep.coffeemanagement.service.goods.GoodsService;
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
//public class GoodsServiceTest {
//  @Autowired
//  private GoodsService goodsService;
//
//  @Test
//  @DisplayName("getListGoods: UTC01")
//  public void testGetListGoodsUTC01() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("status", "1");
//    Optional<ListWrapperResponse<GoodsRes>> actual = goodsService.getListGoods(
//      allParams,
//      "",
//      0,
//      0,
//      "",
//      true
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(22, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("getListGoods: UTC02")
//  public void testGetListGoodsUTC02() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("statuss", "1");
//    Optional<ListWrapperResponse<GoodsRes>> actual = goodsService.getListGoods(
//      allParams,
//      "",
//      0,
//      0,
//      "",
//      true
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(25, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC01 (name over length 200)")
//  public void testCreateGoodsUTC01() {
//    GoodsReq req = new GoodsReq();
//    req.setName(RandomStringUtils.randomAlphabetic(201).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Tên không được vượt quá 200 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC02 (name blank)")
//  public void testCreateGoodsUTC02() {
//    GoodsReq req = new GoodsReq();
//    req.setName("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Tên không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC03 (code blank)")
//  public void testCreateGoodsUTC03() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc03");
//    req.setCode("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Mã không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("code"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC04 (code over length 200)")
//  public void testCreateGoodsUTC04() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc04");
//    req.setCode(RandomStringUtils.randomAlphabetic(201).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Mã không được vượt quá 200 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("code"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC05 (description blank)")
//  public void testCreateGoodsUTC05() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc05");
//    req.setCode("utc05");
//    req.setDescription("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Thông tin không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC06 (description over length 4000)")
//  public void testCreateGoodsUTC06() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc06");
//    req.setCode("utc06");
//    req.setDescription(RandomStringUtils.randomAlphabetic(4001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Thông tin không được vượt quá 4000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC07 (categoryId null)")
//  public void testCreateGoodsUTC07() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc07");
//    req.setCode("utc07");
//    req.setDescription("utc07");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Danh mục không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("categoryId"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC08 (goodsUnit blank)")
//  public void testCreateGoodsUTC08() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc08");
//    req.setCode("utc08");
//    req.setDescription("utc08");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(0);
//    req.setGoodsUnit("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Đơn vị tính không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsUnit"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC09 (isSold = 1, applyPrice <= 0)")
//  public void testCreateGoodsUTC09() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc09");
//    req.setCode("utc09");
//    req.setDescription("utc09");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(0);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(-1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Giá bản của sản phẩm bán phải lớn hơn 0";
//    Assertions.assertEquals(expected, exception.getResult().get("applyPrice"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC10 (isTransfer = 1, innerPrice <= 0)")
//  public void testCreateGoodsUTC10() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc10");
//    req.setCode("utc10");
//    req.setDescription("utc10");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(-1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Giá nhập của sản phẩm nhập phải lớn hơn 0";
//    Assertions.assertEquals(expected, exception.getResult().get("innerPrice"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC11 (name duplicated)")
//  public void testCreateGoodsUTC11() {
//    GoodsReq req = new GoodsReq();
//    req.setName("MOCHA MACCHIATO");
//    req.setCode("utc11");
//    req.setDescription("utc11");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Trùng tên sản phẩm";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsName"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC12 (code duplicated)")
//  public void testCreateGoodsUTC12() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc12");
//    req.setCode("coffe");
//    req.setDescription("utc12");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Trùng mã sản phẩm";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsCode"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC13 (category not found)")
//  public void testCreateGoodsUTC13() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc13");
//    req.setCode("utc13");
//    req.setDescription("utc13");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    ResourceNotFoundException exception = Assert.assertThrows(
//      ResourceNotFoundException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Không tìm thấy danh mục";
//    Assertions.assertEquals(expected, exception.getMessage());
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC14 (image_info with base64 blank)")
//  public void testCreateGoodsUTC14() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc14");
//    req.setCode("utc14");
//    req.setDescription("utc14");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "Không đọc được ảnh";
//    Assertions.assertEquals(expected, exception.getResult().get("base64"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC15 (image_info with prefix blank)")
//  public void testCreateGoodsUTC15() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc15");
//    req.setCode("utc15");
//    req.setDescription("utc15");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.createGoods(req);
//      }
//    );
//    String expected = "File extension không xác định";
//    Assertions.assertEquals(expected, exception.getResult().get("prefix"));
//  }
//
//  @Test
//  @DisplayName("createGoods: UTC16")
//  public void testCreateGoodsUTC16() {
//    GoodsReq req = new GoodsReq();
//    req.setName("utc16");
//    req.setCode("utc16");
//    req.setDescription("utc16");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc16");
//    imageInfoReq.setPrefix("utc16");
//    req.setImage(imageInfoReq);
//    goodsService.createGoods(req);
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC01 (not found goods)")
//  public void testUpdateGoodsUTC01() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("test");
//    ResourceNotFoundException exception = Assert.assertThrows(
//      ResourceNotFoundException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Không tìm thấy sản phẩm";
//    Assertions.assertEquals(expected, exception.getMessage());
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC02 (name over length 200)")
//  public void testUpdateGoodsUTC02() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName(RandomStringUtils.randomAlphabetic(201).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Tên không được vượt quá 200 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC03 (name blank)")
//  public void testUpdateGoodsUTC03() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Tên không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC04 (code blank)")
//  public void testUpdateGoodsUTC04() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc03");
//    req.setCode("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Mã không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("code"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC05 (code over length 200)")
//  public void testUpdateGoodsUTC05() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc04");
//    req.setCode(RandomStringUtils.randomAlphabetic(201).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Mã không được vượt quá 200 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("code"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC06 (description blank)")
//  public void testUpdateGoodsUTC06() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc05");
//    req.setCode("utc05");
//    req.setDescription("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Thông tin không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC07 (description over length 4000)")
//  public void testUpdateGoodsUTC07() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc06");
//    req.setCode("utc06");
//    req.setDescription(RandomStringUtils.randomAlphabetic(4001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Thông tin không được vượt quá 4000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("description"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC08 (categoryId null)")
//  public void testUpdateGoodsUTC08() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc07");
//    req.setCode("utc07");
//    req.setDescription("utc07");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Danh mục không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("categoryId"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC09 (goodsUnit blank)")
//  public void testUpdateGoodsUTC09() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc08");
//    req.setCode("utc08");
//    req.setDescription("utc08");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(0);
//    req.setGoodsUnit("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Đơn vị tính không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsUnit"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC10 (isSold = 1, applyPrice <= 0)")
//  public void testUpdateGoodsUTC10() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc09");
//    req.setCode("utc09");
//    req.setDescription("utc09");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(0);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(-1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Giá bản của sản phẩm bán phải lớn hơn 0";
//    Assertions.assertEquals(expected, exception.getResult().get("applyPrice"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC11 (isTransfer = 1, innerPrice <= 0)")
//  public void testUpdateGoodsUTC11() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc10");
//    req.setCode("utc10");
//    req.setDescription("utc10");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(-1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Giá nhập của sản phẩm nhập phải lớn hơn 0";
//    Assertions.assertEquals(expected, exception.getResult().get("innerPrice"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC12 (name duplicated)")
//  public void testUpdateGoodsUTC12() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("MOCHA MACCHIATO");
//    req.setCode("utc11");
//    req.setDescription("utc11");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Trùng tên sản phẩm";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsName"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC13 (code duplicated)")
//  public void testUpdateGoodsUTC13() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc12");
//    req.setCode("coffe");
//    req.setDescription("utc12");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Trùng mã sản phẩm";
//    Assertions.assertEquals(expected, exception.getResult().get("goodsCode"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC14 (category not found)")
//  public void testUpdateGoodsUTC14() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc13");
//    req.setCode("utc13");
//    req.setDescription("utc13");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("test");
//    ResourceNotFoundException exception = Assert.assertThrows(
//      ResourceNotFoundException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Không tìm thấy danh mục";
//    Assertions.assertEquals(expected, exception.getMessage());
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC15 (image_info with base64 blank)")
//  public void testUpdateGoodsUTC15() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc14");
//    req.setCode("utc14");
//    req.setDescription("utc14");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "Không đọc được ảnh";
//    Assertions.assertEquals(expected, exception.getResult().get("base64"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC16 (image_info with prefix blank)")
//  public void testUpdateGoodsUTC16() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc15");
//    req.setCode("utc15");
//    req.setDescription("utc15");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        goodsService.updateGoods(req);
//      }
//    );
//    String expected = "File extension không xác định";
//    Assertions.assertEquals(expected, exception.getResult().get("prefix"));
//  }
//
//  @Test
//  @DisplayName("updateGoods: UTC17")
//  public void testUpdateGoodsUTC17() {
//    GoodsReq req = new GoodsReq();
//    req.setGoodsId("b6742eb0-9dc3-4dd3-ba11-99e881984233");
//    req.setName("utc16");
//    req.setCode("utc16");
//    req.setDescription("utc16");
//    req.setIsSize(0);
//    req.setIsSold(1);
//    req.setIsTransfer(1);
//    req.setGoodsUnit("1 gói");
//    req.setApplyPrice(1d);
//    req.setInnerPrice(1d);
//    req.setCategoryId("15de661c-bfb6-4710-be1f-2a3be27ec265");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc16");
//    imageInfoReq.setPrefix("utc16");
//    req.setImage(imageInfoReq);
//    goodsService.updateGoods(req);
//  }
//}
