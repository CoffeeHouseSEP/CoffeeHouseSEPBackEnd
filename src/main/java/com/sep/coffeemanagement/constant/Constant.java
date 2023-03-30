package com.sep.coffeemanagement.constant;

public class Constant {
  public static final String BEARER = "Bearer";
  public static final String DEFAULT_PASSWORD = "Abcd@1234";
  public static final String CLIENT_REGISTER = "XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG";
  public static final String CLIENT_FORGOTPASSWORD =
    "XÁC NHẬN KHÔI PHỤC THÔNG TIN NGƯỜI DÙNG";
  public static final String REGISTER = "client-register";
  public static final String FORGOT = "client-forgot";
  public static final String USER_ROLE = "USER";

  public enum REQUEST_STATUS {
    CREATED,
    PENDING,
    APPROVED,
    COMPLETED,
    CANCELLED
  }
}
