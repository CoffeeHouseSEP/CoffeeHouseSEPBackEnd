package com.sep.coffeemanagement.service.news;

import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.dto.news.NewsReq;
import com.sep.coffeemanagement.dto.news.NewsRes;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.image_info.ImageInfo;
import com.sep.coffeemanagement.repository.image_info.ImageInfoRepository;
import com.sep.coffeemanagement.repository.news.News;
import com.sep.coffeemanagement.repository.news.NewsRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl
  extends AbstractService<NewsRepository>
  implements NewsService {
  @Autowired
  private ImageInfoRepository imageInfoRepository;

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
    String newId = UUID.randomUUID().toString();

    ImageInfoReq imageReq = req.getImage();
    imageReq.setObjectId(newId);
    ImageInfo imageInfo = objectMapper.convertValue(imageReq, ImageInfo.class);
    validate(imageReq);

    news.setNewsId(newId);
    news.setCreatedBy(req.getCreatedBy());
    news.setCreatedDate(DateFormat.getCurrentTime());
    news.setStatus(1);
    repository.insertAndUpdate(news, false);
    imageInfoRepository.insertAndUpdate(imageInfo, false);
  }

  @Override
  public void updateNews(NewsReq req) {
    News news = repository
      .getOneByAttribute("newsId", req.getNewsId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    ImageInfo imageInfo = objectMapper.convertValue(req.getImage(), ImageInfo.class);
    imageInfoRepository.insertAndUpdate(imageInfo, true);
    validate(news);
    news.setTitle(req.getTitle());
    news.setContent(req.getContent());
    news.setStatus(req.getStatus());
    repository.insertAndUpdate(news, true);
  }

  @Override
  public void removeNews(String id) {
    News news = repository
      .getOneByAttribute("newsId", id)
      .orElseThrow(() -> new ResourceNotFoundException("not found"));
    repository.removeNews(id);
  }
}
