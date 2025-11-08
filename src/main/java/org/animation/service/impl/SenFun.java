package org.animation.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.animation.entity.*;
import org.animation.service.HtmlParser;
import org.animation.util.StringUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SenFun implements HtmlParser, Serializable {
  private final static String NAME = "森之屋";
  private final static String LOGO_URL = "https://www.mwcy.net/template/dsn2/static/img/logo1.png";
  private final static String BASE_URL = "https://senfun.in/";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getLogoUrl() {
    return LOGO_URL;
  }

  @Override
  public String getBaseUrl() {
    return BASE_URL;
  }

  @Override
  public List<Animation> fetchSearchSync(String keyword, Integer page, Integer size) throws Exception {
    //https://senfun.in/search.html?wd=JOJO
    Element doc = Jsoup.connect(BASE_URL + "search.html?wd=" + keyword).get().body();
    return doc.select("div.module-card-item.module-item").stream().map(item -> {
      String id = item.select("a.module-card-item-poster").attr("href");
      Elements platform = item.select("div.module-card-item-class");
      Elements coverImg = item.select("div.module-item-pic img");
      String coverUrl = coverImg.attr("data-original");
      String titleCn = coverImg.attr("alt");
      Elements infoBox = item.select("div.module-info-item-content");
      String genre = StringUtil.removeUnusedChar(infoBox.get(0).text());
      String actor = StringUtil.removeUnusedChar(infoBox.get(1).text());
      Animation animation = new Animation();
      animation.setId(id);
      animation.setPlatform(platform.get(0).text());
      animation.setCoverUrls(List.of(BASE_URL + coverUrl));
      animation.setTitleCn(titleCn);
      animation.setGenre(StringUtil.removeUnusedChar(genre));
      animation.setActor(StringUtil.removeUnusedChar(actor));
      return animation;
    }).toList();

  }

  @Override
  public AnimationDetail fetchDetailSync(String videoId) throws Exception {
    Element doc = Jsoup.connect(BASE_URL + videoId).get().body();
    //获取播放源
    List<Source> sources = doc.select("div.module-play-list-content.module-play-list-base").stream().map(item -> {
      Source source = new Source();
      List<Episode> episodes = item.select("a").stream().map(link -> {
        Episode episode = new Episode();
        episode.setId(link.attr("href"));
        episode.setTitle(link.text());
        return episode;
      }).toList();
      source.setEpisodes(episodes);
      return source;
    }).toList();
    //获取动画信息
    Elements infoBox = doc.select("div.module-main");
    Elements coverImg = infoBox.select("div.module-item-pic img");
    String cover = coverImg.attr("data-original");
    String titleCn = coverImg.attr("alt");
    String genre = infoBox.select("div.module-info-tag-link").text();
    Elements infoItem = infoBox.select("div.module-info-item");
    String introduction = infoItem.get(0).text();
    String director = StringUtil.removeUnusedChar(infoItem.get(1).select("div.module-info-item-content").text());
    String screenwriter = StringUtil.removeUnusedChar(infoItem.get(2).select("div.module-info-item-content").text());
    String actor = StringUtil.removeUnusedChar(infoItem.get(3).select("div.module-info-item-content").text());
    String airDate = infoItem.get(4).select("div.module-info-item-content").text();
    String duration = infoItem.get(5).select("div.module-info-item-content").text();
    String totalEpisode = infoItem.get(7).select("div.module-info-item-content").text();
    Animation animation = new Animation();
    animation.setCoverUrls(List.of(BASE_URL + cover));
    animation.setTitleCn(titleCn);
    animation.setGenre(StringUtil.removeUnusedChar(genre));
    animation.setDescription(introduction);
    animation.setDirector(director);
    animation.setScreenwriter(screenwriter);
    animation.setActor(actor);
    animation.setAriDate(airDate);
    animation.setDuration(duration);
    animation.setTotalEpisode(Integer.parseInt(totalEpisode));
    return new AnimationDetail(animation, sources);
  }

  @Override
  public PlayInfo fetchPlayInfoSync(String episodeId) throws Exception {
    Element doc = Jsoup.connect(BASE_URL + episodeId).get().body();
    List<Element> script = doc.select("script[type='text/javascript']").stream().filter(item -> item.data().contains("$(document).ready (function (argument)")).toList();
    Pattern pattern = Pattern.compile("url\\s*:\\s*\"([^\"]+)\"");
    Matcher matcher = pattern.matcher(script.get(0).data());
    String tempUrl = "";
    PlayInfo playInfo = new PlayInfo();
    if (matcher.find()) {
      tempUrl = matcher.group(1);
    }
    Connection connect = Jsoup.connect(BASE_URL + tempUrl);
    String body = connect.execute().body();
    Response response = JSON.parseObject(body, Response.class);
    String realPlayUrl = response.getVideo_plays().get(0).get("play_data");
    playInfo.setPlayUri(realPlayUrl);
    playInfo.setId(episodeId);
    return playInfo;
  }

  @Override
  public String fetchRecommendSync(String html) throws Exception {
    return "";
  }

  @Override
  public List<Schedule> fetchWeeklySync() throws Exception {
    return List.of();
  }

  @Data
  private static class Response {
    List<Map<String, String>> video_plays;
    String html_content;
  }
}
