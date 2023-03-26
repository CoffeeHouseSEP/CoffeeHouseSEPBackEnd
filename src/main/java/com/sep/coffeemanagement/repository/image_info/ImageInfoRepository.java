package com.sep.coffeemanagement.repository.image_info;

import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ImageInfoRepository extends BaseRepository<ImageInfo> {
  public String[] imageInfoIgnores = { "id" };

  ImageInfoRepository() {
    this.ignores = imageInfoIgnores;
    this.idField = "id";
  }

  public void removeImageInfo(String id) {
    StringBuilder sb = new StringBuilder(" delete from image_info where id = '");
    sb.append(id);
    sb.append("'");
    jdbcTemplate.execute(sb.toString());
  }
}
