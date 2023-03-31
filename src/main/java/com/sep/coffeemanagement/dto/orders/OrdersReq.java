package com.sep.coffeemanagement.dto.orders;

import com.sep.coffeemanagement.dto.order_detail.OrderDetailReq;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersReq {
  private String ordersId;

  @NotNull(message = "customer not found")
  private String customerId;

  @NotNull(message = "branch not found")
  private String branchId;

  private Date createdDate;
  private double totalPrice;
  private Date shippedDate;

  @NotNull(message = "address is null")
  @Length(max = 2000, message = "address over length(2000)")
  private String address;

  private String couponId;
  private String status;
  private Date approvedDate;
  private Date cancelledDate;
  private String reason;
  private List<OrderDetailReq> listOrderDetail;
}
