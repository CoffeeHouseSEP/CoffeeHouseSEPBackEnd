package com.sep.coffeemanagement.service.news;

import com.sep.coffeemanagement.constant.DateTime;
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
import org.springframework.transaction.annotation.Transactional;

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
                DateFormat.convertDateStringFormat(
                  news.getCreatedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
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
  @Transactional
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
  @Transactional
  public void updateNews(NewsReq req) {
    News news = repository
      .getOneByAttribute("newsId", req.getNewsId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức"));
    ImageInfo imageInfo = imageInfoRepository
      .getOneByAttribute("objectId", req.getNewsId())
      .orElse(null);
    if (imageInfo == null) {
      imageInfo = new ImageInfo();
      imageInfo.setObjectId(req.getNewsId());
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, false);
    } else {
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, true);
    }
    validate(req);
    news.setTitle(req.getTitle());
    news.setContent(req.getContent());
    news.setStatus(req.getStatus());
    repository.insertAndUpdate(news, true);
  }

  @Override
  public void removeNews(String id) {
    News news = repository
      .getOneByAttribute("newsId", id)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tin tức"));
    repository.removeNews(id);
  }
}
