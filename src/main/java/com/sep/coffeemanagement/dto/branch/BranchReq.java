package com.sep.coffeemanagement.dto.branch;

import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchReq {
  private String branchId;

  @NotEmpty
  @NotBlank
  @NotNull(message = "branch name is blank or empty ")
  @Length(max = 200, message = "branch name over length(200)")
  private String name;

  @NotEmpty
  @NotBlank
  @NotNull(message = "branch address is blank or empty ")
  @Length(max = 1000, message = "branch address over length(1000)")
  private String address;

  @Pattern(regexp = TypeValidation.PHONE, message = "invalid phone number")
  private String phoneNumber;

  @NotEmpty
  @NotBlank
  @NotNull(message = "branch description is blank or empty ")
  @Length(max = 8000, message = "branch description over length(8000)")
  private String description;

  @NotNull(message = "branch manager is empty ")
  private String branchManagerId;

  @NotNull(message = "longitude is empty ")
  private String longitude;

  @NotNull(message = "latitude is empty ")
  private String latitude;

  private int status;
  private Date createdDate;
  private Date cancelledDate;

  @NotEmpty(message = "ward is empty")
  @NotBlank(message = "ward is blank")
  @NotNull(message = "ward is null")
  @Length(max = 100, message = "ward over length(100)")
  private String ward;

  @NotEmpty(message = "district is empty")
  @NotBlank(message = "district is blank")
  @NotNull(message = "district is null")
  @Length(max = 100, message = "district over length(100)")
  private String district;

  @NotEmpty(message = "province is empty")
  @NotBlank(message = "province is blank")
  @NotNull(message = "province is null")
  @Length(max = 100, message = "province over length(100)")
  private String province;

  private ImageInfoReq image;
}
