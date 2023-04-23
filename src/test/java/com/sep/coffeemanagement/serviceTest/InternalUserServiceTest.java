package com.sep.coffeemanagement.serviceTest;

import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginReq;
import com.sep.coffeemanagement.dto.internal_user_login.InternalUserLoginRes;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.authentication.Authentication;
import com.sep.coffeemanagement.service.internal_user.InternalUserService;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.nio.charset.StandardCharsets;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class InternalUserServiceTest {
  @Autowired
  InternalUserService service;

  @Autowired
  Authentication authentication;

  @Autowired
  UserRepository repository;

  @Test
  public void testLoginUTC1() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("huyen2101");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    InternalUserLoginRes res = authentication.login(internalUserLoginReq).orElse(null);
    Assert.assertNotNull(res);
    Assert.assertNotNull(res.getToken());
  }

  @Test
  public void testLoginUTC2() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("ducmanh250801");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Mật khẩu không chính xác";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC3() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("bbbbbbbbbbbbbbb");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Không tìm thấy tên đăng nhập";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC4() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC5() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName(null);
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC6() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("huyen2101");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("1234567".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Mật khẩu không đúng định dạng";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC7() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("huyen2101");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC8() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("huyen2101");
    internalUserLoginReq.setLoginPassword(null);
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC9() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("123");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("ZHlAjXTL4&".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Không tìm thấy tên đăng nhập";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC10() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("huyen2101");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("Tuan24061995@".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Mật khẩu không chính xác";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLoginUTC11() {
    InternalUserLoginReq internalUserLoginReq = new InternalUserLoginReq();
    internalUserLoginReq.setLoginName("anhpd");
    internalUserLoginReq.setLoginPassword(
      new String(Base64.encodeBase64("Tuan24061995@".getBytes()), StandardCharsets.UTF_8)
    );
    Exception exception = Assert.assertThrows(
      InvalidRequestException.class,
      () -> {
        authentication.login(internalUserLoginReq);
      }
    );
    String expected = "Người dùng đã bị vô hiệu hóa";
    Assert.assertEquals(expected, exception.getMessage());
  }

  @Test
  public void testLogoutUTC01() {
    String id = "e1e36725-aec6-496d-96a1-94457bd5d39a";
    authentication.logout(id);
    Assert.assertEquals(
      "",
      repository.getOneByAttribute("internalUserId", id).orElse(null).getToken()
    );
  }

  @Test
  public void testLogoutUTC02() {
    String id = "";
    Exception e = Assert.assertThrows(
      ResourceNotFoundException.class,
      () -> {
        authentication.logout(id);
      }
    );
    String expected = "Không tìm thấy người dùng";
    Assert.assertEquals(expected, e.getMessage());
  }
}
