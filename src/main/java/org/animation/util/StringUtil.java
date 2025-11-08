package org.animation.util;

public class StringUtil {
  public static String removeUnusedChar(String str) {
    return str.replaceAll("\\s+", "").replaceAll("/", ",").replaceAll("\\\\", ",");
  }
}
