package org.anime.util;

import java.io.Serializable;

public class StringUtil implements Serializable {
  public static String removeUnusedChar(String str) {
    return str.replaceAll("\\s+", "").replaceAll("/", ",").replaceAll("\\\\", ",");
  }

  public static String removeBlank(String str) {
//    return str.replaceAll("\\s+", "").trim();
    return str.trim();
  }
}
