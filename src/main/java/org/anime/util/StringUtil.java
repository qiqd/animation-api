package org.anime.util;

import java.io.Serializable;

public class StringUtil implements Serializable {
  public static String removeUnusedChar(String str) {
    if (str == null) {
      return "";
    }
    return str.trim().replaceAll("\\s+", ",").replaceAll("/", ",").replaceAll("\\\\", ",");
  }

  public static String removeBlank(String str) {
    if (str == null) {
      return "";
    }
    return str.trim();
  }
}
