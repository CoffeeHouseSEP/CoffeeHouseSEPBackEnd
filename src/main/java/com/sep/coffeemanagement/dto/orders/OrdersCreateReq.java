package com.sep.coffeemanagement.dto.orders;

import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import java.util.List;
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
  @Length(max = 2000, message = "address over length(2000)")
  private String address;

  @NotNull(message = "province is null")
  private String province;

  @NotNull(message = "ward is null")
  private String ward;

  @NotNull(message = "district is null")
  private String district;

  private String couponId;
  private List<OrderDetailReq> listOrderDetail;
}
