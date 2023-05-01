//package com.sep.coffeemanagement.serviceTest;
//
//import com.sep.coffeemanagement.dto.branch.BranchReq;
//import com.sep.coffeemanagement.dto.branch.BranchRes;
//import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
//import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
//import com.sep.coffeemanagement.exception.InvalidRequestException;
//import com.sep.coffeemanagement.exception.ResourceNotFoundException;
//import com.sep.coffeemanagement.service.branch.BranchService;
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
//import java.util.HashMap;
//import java.util.Optional;
//
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@SpringBootTest
//@Transactional
//public class BranchServiceTest {
//    @Autowired
//    private BranchService branchService;
//
//    @Test
//  @DisplayName("getListBranch: UTC01")
//  public void testGetListBranchUTC01() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("status", "1");
//    Optional<ListWrapperResponse<BranchRes>> actual = branchService.getListBranch(
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
//  @DisplayName("getListBranch: UTC02")
//  public void testGetListBranchUTC02() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("statuss", "1");
//    Optional<ListWrapperResponse<BranchRes>> actual = branchService.getListBranch(
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
//    @DisplayName("createBranch: UTC01 (name blank)")
//    public void testCreateBranchUTC01() {
//          BranchReq req = new BranchReq();
//    req.setName("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        branchService.createBranch(req);
//      }
//    );
//    String expected = "Tên không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("name"));
//  }
//
//    @Test
//    @DisplayName("createBranch: UTC02 (name over length 200)")
//    public void testCreateBranchUTC02() {
//        BranchReq req = new BranchReq();
//        req.setName(RandomStringUtils.randomAlphabetic(201).toUpperCase());
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Tên không được vượt quá 200 ký tự";
//        Assertions.assertEquals(expected, exception.getResult().get("name"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC03 (address blank)")
//    public void testCreateBranchUTC03() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Địa chỉ không được để trống";
//        Assertions.assertEquals(expected, exception.getResult().get("address"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC04 (address over length 1000)")
//    public void testCreateBranchUTC04() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress(RandomStringUtils.randomAlphabetic(1001).toUpperCase());
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Địa chỉ không dược vượt quá 1000 ký tự";
//        Assertions.assertEquals(expected, exception.getResult().get("address"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC05 (phone number invalid regex)")
//    public void testCreateBranchUTC05() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("test");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Sai định dạng số điện thoại";
//        Assertions.assertEquals(expected, exception.getResult().get("phoneNumber"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC06 (description blank)")
//    public void testCreateBranchUTC06() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Thông tin không được để trống";
//        Assertions.assertEquals(expected, exception.getResult().get("description"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC07 (description over length 8000)")
//    public void testCreateBranchUTC07() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription(RandomStringUtils.randomAlphabetic(8001).toUpperCase());
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Thông tin không được vượt quá 8000 ký tự";
//        Assertions.assertEquals(expected, exception.getResult().get("description"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC08 (ward blank)")
//    public void testCreateBranchUTC08() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Phường/Xã không được để trống";
//        Assertions.assertEquals(expected, exception.getResult().get("ward"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC09 (district blank)")
//    public void testCreateBranchUTC09() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Quận/Huyện không được để trống";
//        Assertions.assertEquals(expected, exception.getResult().get("district"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC10 (province blank)")
//    public void testCreateBranchUTC10() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Tỉnh/Thành phố không được để trống";
//        Assertions.assertEquals(expected, exception.getResult().get("province"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC11 (manager not found)")
//    public void testCreateBranchUTC11() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("test");
//        ResourceNotFoundException exception = Assert.assertThrows(
//                ResourceNotFoundException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Không tìm thấy quản lý";
//        Assertions.assertEquals(expected, exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC12 (manager inactive)")
//    public void testCreateBranchUTC12() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("bf39bb20-4278-4463-bbcd-183436101180");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Người dùng không hoạt động";
//        Assertions.assertEquals(expected, exception.getResult().get("branchManagerId"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC13 (manager already own branch)")
//    public void testCreateBranchUTC13() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("8a5a4b63-b928-41e1-aa07-0b6a6de93204");
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Người dùng đã quản lý chi nhánh khác";
//        Assertions.assertEquals(expected, exception.getResult().get("branchManagerId"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC14 (image info base64 blank)")
//    public void testCreateBranchUTC14() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("0f002da1-e100-410d-b5d3-f895ac07dc98");
//        ImageInfoReq imageInfoReq = new ImageInfoReq();
//        imageInfoReq.setBase64("");
//        req.setImage(imageInfoReq);
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "Không đọc được ảnh";
//        Assertions.assertEquals(expected, exception.getResult().get("base64"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC15 (image info prefix blank)")
//    public void testCreateBranchUTC15() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("0f002da1-e100-410d-b5d3-f895ac07dc98");
//        ImageInfoReq imageInfoReq = new ImageInfoReq();
//        imageInfoReq.setBase64("test");
//        imageInfoReq.setPrefix("");
//        req.setImage(imageInfoReq);
//        InvalidRequestException exception = Assert.assertThrows(
//                InvalidRequestException.class,
//                () -> {
//                    branchService.createBranch(req);
//                }
//        );
//        String expected = "File extension không xác định";
//        Assertions.assertEquals(expected, exception.getResult().get("prefix"));
//    }
//
//    @Test
//    @DisplayName("createBranch: UTC16")
//    public void testCreateBranchUTC16() {
//        BranchReq req = new BranchReq();
//        req.setName("test");
//        req.setAddress("test");
//        req.setPhoneNumber("0911113052");
//        req.setDescription("test");
//        req.setWard("Đông Hải");
//        req.setDistrict("Lê Chân");
//        req.setProvince("HP");
//        req.setBranchManagerId("0f002da1-e100-410d-b5d3-f895ac07dc98");
//        ImageInfoReq imageInfoReq = new ImageInfoReq();
//        imageInfoReq.setBase64("test");
//        imageInfoReq.setPrefix("test");
//        req.setImage(imageInfoReq);
//                    branchService.createBranch(req);
//    }
//}
