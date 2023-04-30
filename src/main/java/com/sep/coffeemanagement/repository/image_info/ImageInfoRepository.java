package com.sep.coffeemanagement.repository.image_info;

import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.abstract_repository.BaseRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ImageInfoRepository extends BaseRepository<ImageInfo> {
  public String[] imageInfoIgnores = { "id" };

  ImageInfoRepository() {
    this.ignores = imageInfoIgnores;
    this.idField = "id";
  }

  public void removeImageInfo(String id) {
    StringBuilder sb = new StringBuilder(" delete from image_info where id = ");
    sb.append(id);
    jdbcTemplate.execute(sb.toString());
  }

  public ImageInfo getImageByObjectId(String id) {
    StringBuilder sb = new StringBuilder(" select * from image_info where object_id = '");
    sb.append(id);
    sb.append("'");
    List<ImageInfo> lstImageInfo = replaceQuery(sb.toString(), ImageInfo.class)
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
    if (lstImageInfo.isEmpty()) {
      throw new ResourceNotFoundException("Không tìm thấy ảnh");
    } else {
      return lstImageInfo.get(0);
    }
  }
}
