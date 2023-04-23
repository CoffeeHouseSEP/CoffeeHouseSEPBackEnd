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

  @NotNull(message = "Chi nhánh không được để trống")
  private String branchId;

  @NotEmpty(message = "Địa chỉ không được để trống")
  @NotBlank(message = "Địa chỉ không được để trống")
  @NotNull(message = "Địa chỉ không được để trống")
  @Length(max = 1000, message = "Địa chỉ không dược vượt quá 1000 ký tự")
  private String address;

  @NotEmpty(message = "Tỉnh/Thành phố không được để trống")
  @NotBlank(message = "Tỉnh/Thành phố không được để trống")
  @NotNull(message = "Tỉnh/Thành phố không được để trống")
  private String province;

  @NotEmpty(message = "Phường/Xã không được để trống")
  @NotBlank(message = "Phường/Xã không được để trống")
  @NotNull(message = "Phường/Xã không được để trống")
  private String ward;

  @NotEmpty(message = "Quận/Huyện không được để trống")
  @NotBlank(message = "Quận/Huyện không được để trống")
  @NotNull(message = "Quận/Huyện không được để trống")
  private String district;

  private String couponId;

  @Length(max = 1000, message = "Lưu ý không dược vượt quá 1000 ký tự")
  private String description;

  private List<OrderDetailReq> listOrderDetail;
}
