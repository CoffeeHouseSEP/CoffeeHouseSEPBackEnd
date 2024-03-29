package com.sep.coffeemanagement.constant;

public class TypeValidation {
  public static final String USERNAME = "^[a-zA-Z0-9\\\\._\\\\-]{3,}$";
  public static final String PASSWORD =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-!@#&()[{}]:;',?/*~$^+=<>]).{8,20}$";
  public static final String PHONE = "^(((\\+|)84)|0)(3|5|7|8|9)+([0-9]{8})$";
  public static final String DATE = "^\\d{4}-\\d{2}-\\d{2}$";
  public static final String HOUR = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
  public static final String TIME =
    "^\\d{4}-\\d{2}-\\d{2} (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
  public static final String EMAIL =
    "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
  public static final String BASE64_REGEX = "^[a-zA-Z0-9-!@#&()[{}]:;',?/*~$^+=<>]+$";
  public static String PATH_PRE_FIX = "data:image/svg+xml;base64,";
  public static final String FULL_NAME = "^[a-zA-Z\\s]+";
}
