package org.animation.service.impl;

import org.animation.entity.*;
import org.animation.service.HtmlParser;
import org.animation.util.StringUtil;
import org.animation.util.UnicodeUtils;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AiyiFan implements HtmlParser, Serializable {
  final static String NAME = "爱壹番";
  final static String LOGOURL = "https://www.aiyifan.sbs/static/images/logo.jpg";
  final static String BASEURL = "https://www.aiyifan.sbs";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getLogoUrl() {
    return LOGOURL;
  }

  @Override
  public String getBaseUrl() {
    return BASEURL;
  }

  @Override
  public List<Animation> fetchSearchSync(String keyword, Integer page, Integer size) throws Exception {
    String searchUrl = "/ayf.sbssearch/-------------.html?wd=" + keyword;
    Element document = Jsoup.connect(BASEURL + searchUrl).get().body();
    Elements animeItems = document.select("div.details-info-min");
    List<Animation> animeList = new ArrayList<>();
    for (Element it : animeItems) {
      Element imageElement = it.select("a.video-pic").get(0);
      String id = imageElement.attr("href");
      String title = imageElement.attr("title");
      String cover = imageElement.attr("data-original");
      Elements li = it.select("ul.info li");
      String status = li.get(2).text();
      String type = li.get(3).select("a").text();
      StringBuilder actorBuilder = new StringBuilder();
      Elements actorElements = li.get(4).select("a");
      for (int i = 0; i < actorElements.size(); i++) {
        if (i > 0) actorBuilder.append(",");
        actorBuilder.append(actorElements.get(i).text());
      }
      String actor = actorBuilder.toString();
      StringBuilder directorBuilder = new StringBuilder();
      Elements directorElements = li.get(5).select("a");
      for (int i = 0; i < directorElements.size(); i++) {
        if (i > 0) directorBuilder.append(",");
        directorBuilder.append(directorElements.get(i).text());
      }
      String director = directorBuilder.toString();
      String country = li.get(6).text();
      String year = li.get(8).text();
      String updateTime = li.get(7).text();
      String description = li.get(10).text();
      Animation animation = fillAnimation(id, title, cover, status, type, actor, director, country);
      animation.setAriDate(year);
      animation.setDescription(description);
      animeList.add(animation);
    }
    return animeList;
  }

  @Override
  public AnimationDetail fetchDetailSync(String videoId) throws Exception {
    Element doc = Jsoup.connect(BASEURL + videoId).get().body();
    Elements playSource = doc.select("div.playlist-mobile ul.clearfix");
    List<Source> sources = playSource.stream().map(item -> {
      Source source = new Source();
      List<Episode> episodes = item.select("li a").stream().map(i -> {
        Episode episode = new Episode();
        episode.setId(i.attr("href"));
        episode.setTitle(i.text());
        return episode;
      }).collect(Collectors.toList());
      source.setEpisodes(episodes);
      return source;
    }).toList();
    Elements coverImg = doc.select("div.details-pic a");
    String titleCN = coverImg.attr("title");
    String cover = coverImg.attr("style");
    cover = cover.substring(cover.indexOf("(") + 1, cover.indexOf(")"));
    Elements infoBox = doc.select("ul.info.clearfix li");
    String status = infoBox.get(1).text();
    String genre = infoBox.get(2).text();
    String actor = infoBox.get(3).text();
    String director = infoBox.get(3).text();
    String country = infoBox.get(4).text();
    String language = infoBox.get(5).text();
    String ariDate = infoBox.get(6).text();
    String description = doc.select("span.details-content-all").text();
    Animation animation = fillAnimation(videoId, titleCN, cover, status, genre, actor, director, country);
    animation.setLanguage(language);
    animation.setAriDate(ariDate);
    animation.setDescription(description);
    AnimationDetail animationDetail = new AnimationDetail();
    animationDetail.setAnimation(animation);
    animationDetail.setSources(sources);
    return animationDetail;
  }

  private Animation fillAnimation(String videoId, String titleCN, String cover, String status, String genre, String actor, String director, String country) {
    Animation animation = new Animation();
    animation.setId(videoId);
    animation.setTitleCn(titleCN);
    animation.setCoverUrls(List.of(cover));
    animation.setStatus(status);
    animation.setGenre(StringUtil.removeUnusedChar(genre));
    animation.setActor(StringUtil.removeUnusedChar(actor));
    animation.setDirector(StringUtil.removeUnusedChar(director));
    animation.setCountry(country);
    return animation;
  }

  @Override
  @Nullable
  public PlayInfo fetchPlayInfoSync(String episodeId) throws Exception {
    Element doc = Jsoup.connect(BASEURL + episodeId).get().body();
    List<Element> playerAaaa = doc.select("script[type='text/javascript']").stream().filter(item -> item.data().contains("player_aaaa")).toList();
    String playData = playerAaaa.get(0).data();
    Pattern pattern = Pattern.compile("\"url\"\\s*:\\s*\"([^\"]*)\"");
    Matcher matcher = pattern.matcher(playData);
    if (matcher.find()) {
      String playUri = matcher.group(1).replaceAll("\\\\", "");
      PlayInfo playInfo = new PlayInfo();
      playInfo.setId(episodeId);
      playInfo.setPlayUri(UnicodeUtils.decodeUnicode(playUri));
      return playInfo;
    }
    return null;
  }

  @Override
  public String fetchRecommendSync(String html) throws Exception {
    return "";
  }

  @Override
  public List<Schedule> fetchWeeklySync() throws Exception {
    return List.of();
  }
}
