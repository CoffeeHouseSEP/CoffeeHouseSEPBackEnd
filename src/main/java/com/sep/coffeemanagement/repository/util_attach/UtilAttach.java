package com.sep.coffeemanagement.repository.util_attach;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilAttach {
  private int utilAttachId;
  private String type;
  private int objectId;
  private int uploadBy;
  private Date uploadDate;
  private String encrFilePath;
  private int status;
}
