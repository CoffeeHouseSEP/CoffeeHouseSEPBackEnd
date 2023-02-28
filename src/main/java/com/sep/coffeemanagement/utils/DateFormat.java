package com.sep.coffeemanagement.utils;

import com.sep.coffeemanagement.constant.TypeValidation;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DateFormat {

  public static Date getCurrentTime() {
    Date date = java.util.Calendar.getInstance().getTime();
    return date;
  }

  public static String toDateString(Date date, String format) {
    if (date == null) return "";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String result = null;
    try {
      result = sdf.format(date);
    } catch (Exception e) {
      throw new InvalidRequestException(new HashMap<>(), "Invalid date format");
    }
    return result;
  }

  public static Date convertStringToDate(String date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    Date result = null;
    try {
      result = sdf.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static String combineDateAndHour(String date, String hour) {
    String combineDateAndHourString = date + " " + hour;
    if (!combineDateAndHourString.matches(TypeValidation.TIME)) {
      throw new InvalidRequestException(new HashMap<>(), "Invalid date format");
    }
    return combineDateAndHourString;
  }
}
