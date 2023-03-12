package com.sep.coffeemanagement.service.news;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.news.NewsReq;
import com.sep.coffeemanagement.dto.news.NewsRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.news.News;
import com.sep.coffeemanagement.repository.news.NewsRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl
  extends AbstractService<NewsRepository>
  implements NewsService {

  @Override
  public Optional<ListWrapperResponse<NewsRes>> getListNews(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<NewsRes> list = repository.getListNews(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            news ->
              new NewsRes(
                news.getNewsId(),
                news.getTitle(),
                news.getContent(),
                news.getCreatedBy(),
                news.getCreatedDate(),
                news.getStatus(),
                news.getCreatedByName()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createNews(NewsReq req) {
    validate(req);
    News news = objectMapper.convertValue(req, News.class);
    news.setNewsId(0);
    news.setCreatedDate(DateFormat.getCurrentTime());
    news.setStatus(1);
    repository.insertAndUpdate(news, false);
  }

  @Override
  public void updateNews(NewsReq req) {
    News news = repository
      .getOneByAttribute("newsId", Integer.toString(req.getNewsId()))
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    validate(news);
    news.setTitle(req.getTitle());
    news.setContent(req.getContent());
    news.setStatus(req.getStatus());
    repository.insertAndUpdate(news, true);
  }
}
