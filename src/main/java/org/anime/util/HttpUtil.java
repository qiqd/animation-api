package org.anime.util;

import org.anime.entity.bangmi.SourceWithDelay;
import org.anime.loger.Logger;
import org.anime.loger.LoggerFactory;
import org.anime.parser.HtmlParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpUtil implements Serializable {
  private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

  /**
   * 创建配置好的Jsoup连接对象，模拟浏览器请求
   *
   * @return 配置好的Connection对象
   */
  public static Connection createConnection(String url) {
    return new HttpConnection()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
//            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//            .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
//            .header("Accept-Encoding", "gzip, deflate, br")
//            .header("Connection", "keep-alive")
//            .header("Upgrade-Insecure-Requests", "1")
//            .header("Sec-Fetch-Dest", "document")
//            .header("Sec-Fetch-Mode", "navigate")
//            .header("Sec-Fetch-Site", "none")
//            .header("Cache-Control", "max-age=0")
            .timeout(20000); // 20秒超时
  }

  public static Connection createConnection(String url, String referer) {
    return createConnection(url)
            .header("Referer", referer);
  }

  public static void delayTest(List<SourceWithDelay> delays, Map<String, ? extends HtmlParser> sources) {
    sources.clear();
    ExecutorService executor = Executors.newCachedThreadPool();
    CountDownLatch latch = new CountDownLatch(sources.size());
    sources.forEach((name, parser) -> {
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
          delays.add(new SourceWithDelay(delay, parser));
          delays.sort(Comparator.comparingInt(SourceWithDelay::getDelay));
        } catch (Exception e) {
          log.error("Error while initializing source:{},err msg: {}", name, e.getMessage());
          delays.add(new SourceWithDelay(999999, parser));
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
