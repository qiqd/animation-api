package org.anime.loger;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerFactory implements Serializable {
  public static Logger getLogger(Class<?> clazz) {
    return new SimpleLogger(clazz.getName());
  }

  public static Logger getLogger(String name) {
    return new SimpleLogger(name);
  }

  private static class SimpleLogger implements Logger, Serializable {
    private final String name;
    private static final LogLevel CURRENT_LEVEL = LogLevel.INFO;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SimpleLogger(String name) {
      this.name = name;
    }

    @Override
    public void debug(String message) {
      if (isDebugEnabled()) {
        log(LogLevel.DEBUG, message);
      }
    }

    @Override
    public void debug(String format, Object... args) {
      if (isDebugEnabled()) {
        log(LogLevel.DEBUG, format(format, args));
      }
    }

    @Override
    public void info(String message) {
      if (isInfoEnabled()) {
        log(LogLevel.INFO, message);
      }
    }

    @Override
    public void info(String format, Object... args) {
      if (isInfoEnabled()) {
        log(LogLevel.INFO, format(format, args));
      }
    }

    @Override
    public void warn(String message) {
      if (isWarnEnabled()) {
        log(LogLevel.WARN, message);
      }
    }

    @Override
    public void warn(String format, Object... args) {
      if (isWarnEnabled()) {
        log(LogLevel.WARN, format(format, args));
      }
    }

    @Override
    public void error(String message) {
      if (isErrorEnabled()) {
        log(LogLevel.ERROR, message);
      }
    }

    @Override
    public void error(String format, Object... args) {
      if (isErrorEnabled()) {
        log(LogLevel.ERROR, format(format, args));
      }
    }

    @Override
    public void error(String message, Throwable throwable) {
      if (isErrorEnabled()) {
        log(LogLevel.ERROR, message + " - " + throwable.getMessage());
        throwable.printStackTrace(System.err);
      }
    }

    @Override
    public boolean isDebugEnabled() {
      return CURRENT_LEVEL.getLevel() <= LogLevel.DEBUG.getLevel();
    }

    @Override
    public boolean isInfoEnabled() {
      return CURRENT_LEVEL.getLevel() <= LogLevel.INFO.getLevel();
    }

    @Override
    public boolean isWarnEnabled() {
      return CURRENT_LEVEL.getLevel() <= LogLevel.WARN.getLevel();
    }

    @Override
    public boolean isErrorEnabled() {
      return CURRENT_LEVEL.getLevel() <= LogLevel.ERROR.getLevel();
    }

    private void log(LogLevel level, String message) {
      String timestamp = LocalDateTime.now().format(FORMATTER);
      String threadName = Thread.currentThread().getName();
      System.out.printf("%s [%s] %s %s - %s%n",
              timestamp, threadName, level.name(), name, message);
    }

    private String format(String format, Object... args) {
      if (args == null || args.length == 0) {
        return format;
      }
      return String.format(format, args);
    }
  }
}
