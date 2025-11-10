package org.anime.api;

import org.anime.entity.bangmi.SourceWithDelay;
import org.anime.loger.Logger;
import org.anime.loger.LoggerFactory;
import org.anime.parser.HtmlParser;
import org.anime.parser.impl.animation.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AnimeApi implements Serializable {
  private static final Logger log = LoggerFactory.getLogger(AnimeApi.class);

  public static final ArrayList<SourceWithDelay> SOURCES_WITH_DELAY = new ArrayList<>();
  public static final HashMap<String, HtmlParser> SOURCE_MAP = new HashMap<>();

  static {
    SOURCE_MAP.put(AAFun.NAME, new AAFun());
    SOURCE_MAP.put(AiyiFan.NAME, new AiyiFan());
    SOURCE_MAP.put(GirigiriLove.NAME, new GirigiriLove());
    SOURCE_MAP.put(MengDao.NAME, new MengDao());
    SOURCE_MAP.put(Mwcy.NAME, new Mwcy());
    SOURCE_MAP.put(SenFun.NAME, new SenFun());
  }

  public static void moveToTop(int index) {
    if (index < 0 || index >= SOURCES_WITH_DELAY.size()) {
      return;
    }
    SourceWithDelay sourceWithDelay = SOURCES_WITH_DELAY.remove(index);
    SOURCES_WITH_DELAY.add(0, sourceWithDelay);
  }

  public static void initialization() {
    SOURCES_WITH_DELAY.clear();
    ExecutorService executor = Executors.newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(SOURCE_MAP.size());
    SOURCE_MAP.forEach((name, parser) -> {
      executor.submit(() -> {
        try {
          long startTime = System.currentTimeMillis();
          Connection connection = Jsoup.connect(parser.getBaseUrl())
                  .timeout(5000)
                  .followRedirects(true)
                  .ignoreContentType(true);
          Connection.Response response = connection.execute();
          long endTime = System.currentTimeMillis();
          int delay = -1;
          if (response.statusCode() == 200) {
            delay = (int) (endTime - startTime);
          }
          SOURCES_WITH_DELAY.add(new SourceWithDelay(delay, parser));
          SOURCES_WITH_DELAY.sort(Comparator.comparingInt(SourceWithDelay::getDelay));
        } catch (Exception e) {
          log.error("Error while initializing source:{},err msg: {}", name, e.getMessage());
          SOURCES_WITH_DELAY.add(new SourceWithDelay(-999999, parser));
        } finally {
          latch.countDown();
        }
      });
    });
    try {
      boolean completed = latch.await(10, TimeUnit.SECONDS);
      if (!completed) {
        log.warn("Initialization did not complete within the timeout period.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Initialization interrupted.", e);
    } finally {
      executor.shutdown();
    }
  }
}
