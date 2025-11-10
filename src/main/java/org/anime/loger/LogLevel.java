package org.anime.loger;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum LogLevel implements Serializable {
  DEBUG(0),
  INFO(1),
  WARN(2),
  ERROR(3);

  private final int level;

  LogLevel(int level) {
    this.level = level;
  }

}
