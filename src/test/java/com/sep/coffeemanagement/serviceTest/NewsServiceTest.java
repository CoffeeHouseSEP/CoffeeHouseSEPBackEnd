//package com.sep.coffeemanagement.serviceTest;
//
//import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
//import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
//import com.sep.coffeemanagement.dto.news.NewsReq;
//import com.sep.coffeemanagement.dto.news.NewsRes;
//import com.sep.coffeemanagement.exception.InvalidRequestException;
//import com.sep.coffeemanagement.exception.ResourceNotFoundException;
//import com.sep.coffeemanagement.service.news.NewsService;
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
//public class NewsServiceTest {
//  @Autowired
//  private NewsService newsService;
//
//  @Test
//  @DisplayName("getListNews: UTC01")
//  public void testGetListNewsUTC01() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("status", "1");
//    Optional<ListWrapperResponse<NewsRes>> actual = newsService.getListNews(
//      allParams,
//      "",
//      1,
//      10,
//      ""
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(7, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("getListNews: UTC02")
//  public void testGetListNewsUTC02() {
//    HashMap<String, String> allParams = new HashMap<>();
//    allParams.put("statuss", "1");
//    Optional<ListWrapperResponse<NewsRes>> actual = newsService.getListNews(
//      allParams,
//      "",
//      1,
//      10,
//      ""
//    );
//    Assertions.assertTrue(actual.isPresent());
//    Assertions.assertNotNull(actual.get().getData());
//    Assertions.assertEquals(9, actual.get().getData().size());
//  }
//
//  @Test
//  @DisplayName("createNews: UTC01 (title blank)")
//  public void testCreateNewsUTC01() {
//    NewsReq req = new NewsReq();
//    req.setTitle("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "Tiêu đề không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("title"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC02 (title over length 2000)")
//  public void testCreateNewsUTC02() {
//    NewsReq req = new NewsReq();
//    req.setTitle(RandomStringUtils.randomAlphabetic(2001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "Tiêu đề không được vượt quá 2000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("title"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC03 (content blank)")
//  public void testCreateNewsUTC03() {
//    NewsReq req = new NewsReq();
//    req.setTitle("test");
//    req.setContent("");
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "Nội dung không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("content"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC04 (content over length 16000)")
//  public void testCreateNewsUTC04() {
//    NewsReq req = new NewsReq();
//    req.setTitle("test");
//    req.setContent(RandomStringUtils.randomAlphabetic(16001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "Nội dung không được vượt quá 16000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("content"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC05 (image_info with base64 blank)")
//  public void testCreateNewsUTC05() {
//    NewsReq req = new NewsReq();
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "Không đọc được ảnh";
//    Assertions.assertEquals(expected, exception.getResult().get("base64"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC06 (image_info with prefix blank)")
//  public void testCreateNewsUTC06() {
//    NewsReq req = new NewsReq();
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//      InvalidRequestException.class,
//      () -> {
//        newsService.createNews(req);
//      }
//    );
//    String expected = "File extension không xác định";
//    Assertions.assertEquals(expected, exception.getResult().get("prefix"));
//  }
//
//  @Test
//  @DisplayName("createNews: UTC07")
//  public void testCreateNewsUTC07() {
//    NewsReq req = new NewsReq();
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("utc");
//    req.setImage(imageInfoReq);
//    newsService.createNews(req);
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC01 (not found news_id)")
//  public void testUpdateNewsUTC01() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("test");
//    ResourceNotFoundException exception = Assert.assertThrows(
//            ResourceNotFoundException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Không tìm thấy tin tức";
//    Assertions.assertEquals(expected, exception.getMessage());
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC02 (title blank)")
//  public void testUpdateNewsUTC02() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("");
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Tiêu đề không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("title"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC03 (title over length 2000)")
//  public void testUpdateNewsUTC03() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle(RandomStringUtils.randomAlphabetic(2001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Tiêu đề không được vượt quá 2000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("title"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC04 (content blank)")
//  public void testUpdateNewsUTC04() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("test");
//    req.setContent("");
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Nội dung không được để trống";
//    Assertions.assertEquals(expected, exception.getResult().get("content"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC05 (content over length 16000)")
//  public void testUpdateNewsUTC05() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("test");
//    req.setContent(RandomStringUtils.randomAlphabetic(16001).toUpperCase());
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Nội dung không được vượt quá 16000 ký tự";
//    Assertions.assertEquals(expected, exception.getResult().get("content"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC06 (image_info with base64 blank)")
//  public void testUpdateNewsUTC06() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "Không đọc được ảnh";
//    Assertions.assertEquals(expected, exception.getResult().get("base64"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC07 (image_info with prefix blank)")
//  public void testUpdateNewsUTC07() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("");
//    req.setImage(imageInfoReq);
//    InvalidRequestException exception = Assert.assertThrows(
//            InvalidRequestException.class,
//            () -> {
//              newsService.updateNews(req);
//            }
//    );
//    String expected = "File extension không xác định";
//    Assertions.assertEquals(expected, exception.getResult().get("prefix"));
//  }
//
//  @Test
//  @DisplayName("updateNews: UTC08")
//  public void testUpdateNewsUTC08() {
//    NewsReq req = new NewsReq();
//    req.setNewsId("1be48116-f357-4163-b148-481d05a03f92");
//    req.setTitle("test");
//    req.setContent("test");
//    ImageInfoReq imageInfoReq = new ImageInfoReq();
//    imageInfoReq.setBase64("utc15");
//    imageInfoReq.setPrefix("utc");
//    req.setImage(imageInfoReq);
//    newsService.updateNews(req);
//  }
//}
