package org.anime.parser;

import org.anime.entity.animation.Schedule;
import org.anime.entity.comic.Comic;
import org.anime.entity.comic.ComicDetail;
import org.anime.entity.comic.ComicPage;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * HTML解析接口规范
 */
public interface ComicParser extends Serializable {

  /**
   * 名称
   */
  String getName();

  /**
   * 网站logo地址
   */
  String getLogoUrl();

  /**
   * 网站地址
   */
  String getBaseUrl();

  /**
   * 解析搜索结果
   *
   * @param keyword 搜索关键词
   * @param page    页码
   * @param size    每页数量
   * @return List<Comic>
   * @throws Exception 解析异常
   */

  List<Comic> fetchSearchSync(String keyword, Integer page, Integer size) throws Exception;

  /**
   * 解析详情信息
   *
   * @param comicId 漫画ID
   * @return ComicDetail
   * @throws Exception 解析异常
   */
  @Nullable
  ComicDetail fetchDetailSync(String comicId) throws Exception;

  /**
   * 解析播放信息
   *
   * @param chapterId  章节id
   * @param pageNumber 页码
   * @return ComicPage列表
   * @throws Exception 解析异常
   */
  List<ComicPage> fetchPageSync(String chapterId, Integer pageNumber) throws Exception;

  /**
   * 解析推荐列表
   *
   * @param html HTML内容
   * @return 推荐漫画列表
   * @throws Exception 解析异常
   */
  String fetchRecommendSync(String html) throws Exception;

  /**
   * 获取每周更新时间表
   *
   * @return Schedule列表
   * @throws Exception 解析异常
   */
  List<Schedule> fetchWeeklySync() throws Exception;
}
