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
  public static final String ADMIN_ROLE = "ADMIN";
  public static final String BRANCH_ROLE = "BRANCH_MANAGER";
  public static final String ORDER_STATUS_SUMMARY_REVENUE = "APPROVED";

  public enum REQUEST_STATUS {
    CREATED,
    PENDING,
    APPROVED,
    COMPLETED,
    CANCELLED
  }

  public enum ORDER_STATUS {
    PENDING_APPROVED,
    APPROVED,
    CANCELLED
  }

  public enum GOODS_SIZE {
    S,
    M,
    L
  }
}
