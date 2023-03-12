package com.sep.coffeemanagement.service.news;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.news.NewsReq;
import com.sep.coffeemanagement.dto.news.NewsRes;
import java.util.Map;
import java.util.Optional;

public interface NewsService {
  Optional<ListWrapperResponse<NewsRes>> getListNews(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  );

  void createNews(NewsReq req);

  void updateNews(NewsReq req);
}
