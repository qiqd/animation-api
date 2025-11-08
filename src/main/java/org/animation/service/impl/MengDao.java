package org.animation.service.impl;

import org.animation.entity.*;
import org.animation.service.HtmlParser;
import org.animation.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MengDao implements HtmlParser, Serializable {
  final static String NAME = "萌岛动漫";
  final static String LOGOURL = "https://www.mengdao.tv/templets/mengdao/images/logo.png";
  final static String BASEURL = "https://www.mengdao.tv";
  private static final String AES_IV = "2023062720230627";
  private static final String AES_KEY = "Mann20230627daoo";
  private static final String AES_MODE = "AES/CBC/ZeroPadding"; // 加密模式

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
//    https://www.mengdao.tv/search.php?searchword=未来日记
    String searchUrl = BASEURL + "/search.php?searchword=" + keyword;
    Element doc = Jsoup.connect(searchUrl).post().body();
    Elements lis = doc.select("div.index-tj.mb.clearfix ul li");
    return lis.stream().map(item -> {
      String id = item.select("a.li-hv").attr("href");
      Elements img = item.select("div.img img");
      String cover = img.attr("data-original");
      String titleCn = img.attr("alt");
      Elements status = item.select("p.bz");
      String statusText = status.text();
      Animation animation = new Animation();
      animation.setId(id);
      animation.setTitleCn(titleCn);
      animation.setStatus(statusText);
      animation.setCoverUrls(List.of(cover));
      return animation;
    }).collect(Collectors.toList());
  }

  @Override
  public AnimationDetail fetchDetailSync(String videoId) throws Exception {
    Element doc = Jsoup.connect(BASEURL + videoId).get().body();
    Elements episodeBox = doc.select("div.plist.clearfix");
    List<Source> sources = episodeBox.stream().map(item -> {
      Source source = new Source();
      List<Episode> episodes = item.select("ul.urlli li").stream().map(i -> {
        Elements a = i.select("a");
        Episode episode = new Episode();
        episode.setId(a.attr("href"));
        episode.setTitle(a.text());
        return episode;
      }).toList();
      source.setEpisodes(episodes);
      return source;
    }).toList();
    Elements img = doc.select("div.pic img");
    String cover = img.attr("src");
    String titleCn = img.attr("alt");
    Elements infoBox = doc.select("div.info");
    String status = infoBox.select("dt.name span").text();
    String subStatus = infoBox.select("dd.m-yc360").text();
    status += subStatus;
    List<Element> infoItem = infoBox.select("dd").stream().skip(1).toList();
    String genre = infoItem.get(0).text();
    String subGenre = infoItem.get(1).text();
    genre += subGenre;
    String cast = infoItem.get(2).text();
    String role = infoItem.get(3).text();
    String otherName = infoItem.get(4).text();
    String description = Optional.ofNullable(infoBox.select("dt").last()).orElse(new Element("dt")).text();
    Animation animation = new Animation();
    animation.setId(videoId);
    animation.setTitleCn(titleCn);
    animation.setStatus(status);
    animation.setCoverUrls(List.of(cover));
    animation.setGenre(StringUtil.removeUnusedChar(genre).substring(3));
    animation.setDescription(description.substring(3));
    animation.setActor(StringUtil.removeUnusedChar(cast).substring(3));
    animation.setRole(StringUtil.removeUnusedChar(role).substring(3));
    animation.setTitleEn(otherName.substring(3).trim());
    return new AnimationDetail(animation, sources);
  }

  @Override
  public PlayInfo fetchPlayInfoSync(String episodeId) throws Exception {
    Element doc = Jsoup.connect(BASEURL + episodeId).get().body();
    List<Element> scripts = doc.select("script").stream().filter(item -> item.data().contains("base64decode")).toList();
    String data = scripts.get(0).data();
    String videoInfo = data.substring(data.indexOf("(") + 2, data.lastIndexOf(")") - 1);
    byte[] decode = Base64.getDecoder().decode(videoInfo);
    // 创建AES密钥和IV参数
    String decodedVideoInfo = new String(decode, StandardCharsets.UTF_8);
    SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
    // 使用NoPadding模式
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    byte[] base64Decoded = Base64.getDecoder().decode(decodedVideoInfo);
    // 执行AES解密
    byte[] decryptedBytes = cipher.doFinal(base64Decoded);
    // 检查末尾填充情况
    int zeroCount = 0;
    for (int i = decryptedBytes.length - 1; i >= 0; i--) {
      if (decryptedBytes[i] == 0) {
        zeroCount++;
      } else {
        break;
      }
    }
    // 手动去除ZeroPadding填充（ZeroPadding在末尾补0）
    int endIndex = decryptedBytes.length;
    for (int i = decryptedBytes.length - 1; i >= 0; i--) {
      if (decryptedBytes[i] != 0) {
        endIndex = i + 1;
        break;
      }
    }
    // 复制有效数据
    byte[] validData = new byte[endIndex];
    System.arraycopy(decryptedBytes, 0, validData, 0, endIndex);
    String result = new String(validData, StandardCharsets.UTF_8);
    VideoData videoData = parseVideoData(result);
    String currentVideoUrl = getCurrentVideoUrl(videoData, BASEURL + episodeId);
    PlayInfo playInfo = new PlayInfo();
    playInfo.setId(episodeId);
    playInfo.setPlayUri(currentVideoUrl);
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

  private static VideoData parseVideoData(String decryptedData) {
    VideoData videoData = new VideoData();

    // 按$$$分割不同的播放源
    String[] sources = decryptedData.split("\\$\\$\\$");

    for (String source : sources) {
      // 按$$分割播放源名称和视频列表
      String[] parts = source.split("\\$\\$");
      if (parts.length >= 2) {
        String sourceName = parts[0]; // 播放源名称，如"云播放"、"极速播放"
        String videoList = parts[1];  // 视频列表数据

        // 解析视频列表
        List<VideoEpisode> episodes = new ArrayList<>();

        // 按$xxx#模式分割不同的集数
        String[] episodeBlocks = videoList.split("\\$\\w+#");
        for (String block : episodeBlocks) {
          // 按$分割集数信息
          String[] episodeInfo = block.split("\\$");
          if (episodeInfo.length >= 3) {
            VideoEpisode episode = new VideoEpisode();
            episode.title = episodeInfo[0];      // 集数标题，如"第1集"
            episode.m3u8Url = episodeInfo[1];  // M3U8地址
            episode.playerType = episodeInfo[2]; // 播放器类型
            episodes.add(episode);
          }
        }

        videoData.sources.put(sourceName, episodes);
      }
    }

    return videoData;
  }

  private static class VideoData {
    // 播放源映射: 播放源名称 -> 视频集数列表
    public java.util.Map<String, List<VideoEpisode>> sources = new java.util.HashMap<>();

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String source : sources.keySet()) {
        sb.append("播放源: ").append(source).append("\n");
        List<VideoEpisode> episodes = sources.get(source);
        for (int i = 0; i < episodes.size(); i++) {
          sb.append("  第").append(i + 1).append("集: ")
                  .append(episodes.get(i).title).append(" - ")
                  .append(episodes.get(i).m3u8Url).append("\n");
        }
      }
      return sb.toString();
    }
  }

  private static class VideoEpisode {
    public String title;      // 集数标题，如"第1集"
    public String m3u8Url;    // M3U8播放地址
    public String playerType; // 播放器类型

    @Override
    public String toString() {
      return String.format("VideoEpisode{title='%s', m3u8Url='%s', playerType='%s'}",
              title, m3u8Url, playerType);
    }
  }

  private static String getCurrentVideoUrl(VideoData videoData, String pageUrl) throws Exception {
    // 从URL中提取当前集数信息
    // URL格式: /man_v/14495-0-0.html -> 14495是视频ID, 第一个0是播放源, 第二个0是第几集
    Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)\\.html");
    Matcher matcher = pattern.matcher(pageUrl);

    if (matcher.find()) {
      String videoId = matcher.group(1);
      int sourceIndex = Integer.parseInt(matcher.group(2));
      int episodeIndex = Integer.parseInt(matcher.group(3));

      // 获取第一个播放源（通常是"云播放"）
      List<String> sourceNames = new ArrayList<>(videoData.sources.keySet());
      if (sourceIndex < sourceNames.size()) {
        String sourceName = sourceNames.get(sourceIndex);
        List<VideoEpisode> episodes = videoData.sources.get(sourceName);

        if (episodeIndex < episodes.size()) {
          VideoEpisode currentEpisode = episodes.get(episodeIndex);
          return currentEpisode.m3u8Url;
        }
      }
    }
    return null;
  }
}
