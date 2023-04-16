package com.sep.coffeemanagement.repository.news;

import com.sep.coffeemanagement.dto.news.NewsRes;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class NewsRepository extends BaseRepository<News> {
  public String[] newsIgnores = { "" };

  NewsRepository() {
    this.ignores = newsIgnores;
    this.idField = "newsId";
  }

  public Optional<NewsRes> getNews(int id) {
    StringBuilder sb = new StringBuilder(
      " select t1.*, t2.full_name createdByName from "
    );
    sb.append(" news t1 ");
    sb.append(" left join internal_user t2 on t1.created_by = t2.internal_user_id ");
    sb.append(" where t1.news_id = ");
    sb.append(id);
    return Optional.of(replaceQuery(sb.toString(), NewsRes.class).get().get(0));
  }

  public List<NewsRes> getListNews(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    StringBuilder sb = new StringBuilder(" select T1.*, ");
    sb.append(
      " (select full_name from internal_user where internal_user_id = T1.created_by) createdByName from"
    );
    sb.append(" news T1 ");
    sb.append(
      convertParamsFilterSelectQuery(
        allParams,
        News.class,
        page,
        pageSize,
        keySort,
        sortField,
        this.idField
      )
    );
    return replaceQuery(sb.toString(), NewsRes.class).get();
  }

  public void removeNews(String newsId) {
    StringBuilder sb = new StringBuilder(" delete from news where news_id = '");
    sb.append(newsId);
    sb.append("'");
    jdbcTemplate.execute(sb.toString());
  }
}
