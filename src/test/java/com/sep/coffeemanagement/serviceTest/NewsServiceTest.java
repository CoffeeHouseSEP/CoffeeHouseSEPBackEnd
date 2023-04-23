//package com.sep.coffeemanagement.serviceTest;
//
//import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
//import com.sep.coffeemanagement.dto.news.NewsRes;
//import com.sep.coffeemanagement.service.news.NewsService;
//import java.util.HashMap;
//import java.util.Optional;
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
//    Assertions.assertEquals(5, actual.get().getData().size());
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
//    Assertions.assertEquals(5, actual.get().getData().size());
//  }
//}
