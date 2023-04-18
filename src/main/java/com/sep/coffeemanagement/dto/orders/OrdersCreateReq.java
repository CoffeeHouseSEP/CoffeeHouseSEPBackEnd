package com.sep.coffeemanagement.dto.orders;

import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersCreateReq {
  private String customerId;

  @NotNull(message = "branch not found")
  private String branchId;

  @NotNull(message = "address is null")
  @NotBlank(message = "address is blank")
  @NotEmpty(message = "address is empty")
  @Length(max = 2000, message = "address over length(2000)")
  private String address;

  @NotNull(message = "province is null")
  @NotBlank(message = "province is blank")
  @NotEmpty(message = "province is empty")
  private String province;

  @NotNull(message = "ward is null")
  @NotBlank(message = "ward is blank")
  @NotEmpty(message = "ward is empty")
  private String ward;

  @NotNull(message = "district is null")
  @NotBlank(message = "district is blank")
  @NotEmpty(message = "district is empty")
  private String district;

  private String couponId;
  private List<OrderDetailReq> listOrderDetail;
}
