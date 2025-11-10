package org.anime.loger;


import java.io.Serializable;

public interface Logger extends Serializable {
  void debug(String message);

  void debug(String format, Object... args);

  void info(String message);

  void info(String format, Object... args);

  void warn(String message);

  void warn(String format, Object... args);

  void error(String message);

  void error(String format, Object... args);

  void error(String message, Throwable throwable);

  boolean isDebugEnabled();

  boolean isInfoEnabled();

  boolean isWarnEnabled();

  boolean isErrorEnabled();
}