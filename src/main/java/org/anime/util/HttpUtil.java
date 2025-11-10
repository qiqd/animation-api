package org.anime.util;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.Serializable;

public class HttpUtil implements Serializable {
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

}
