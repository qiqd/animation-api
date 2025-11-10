package org.anime.util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode转码工具类
 */
public class UnicodeUtils implements Serializable {

  /**
   * 将包含Unicode转义序列的字符串转换为普通字符串
   * 支持格式：\\uXXXX 或 uXXXX
   *
   * @param unicodeStr 包含Unicode编码的字符串
   * @return 解码后的普通字符串
   */
  public static String decodeUnicode(String unicodeStr) {
    if (unicodeStr == null || unicodeStr.isEmpty()) {
      return unicodeStr;
    }
    // 匹配 \\uXXXX 格式（有反斜杠）
    Pattern pattern1 = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
    Matcher matcher1 = pattern1.matcher(unicodeStr);
    StringBuffer stringBuffer = stringMatcher(matcher1);
    // 匹配 uXXXX 格式（无反斜杠）
    Pattern pattern2 = Pattern.compile("u([0-9a-fA-F]{4})");
    Matcher matcher2 = pattern2.matcher(stringBuffer.toString());
    stringBuffer = stringMatcher(matcher2);
    return stringBuffer.toString();
  }

  private static StringBuffer stringMatcher(Matcher matcher) {
    StringBuffer stringBuffer = new StringBuffer();

    while (matcher.find()) {
      try {
        char ch = (char) Integer.parseInt(matcher.group(1), 16);
        matcher.appendReplacement(stringBuffer, String.valueOf(ch));
      } catch (NumberFormatException e) {
        matcher.appendReplacement(stringBuffer, matcher.group(0));
      }
    }
    matcher.appendTail(stringBuffer);
    return stringBuffer;
  }
}
