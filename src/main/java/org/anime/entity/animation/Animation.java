package org.anime.entity.animation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.anime.entity.base.Media;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Animation extends Media implements Serializable {

  /**
   * 子ID，可能用于区分同一动画的不同版本，该字段不从html解析获得
   */
  private Integer subId;


  /**
   * 导演信息
   */
  private String director;

  /**
   * 主演信息
   */
  private String actor;

  /**
   * 首播日期
   */
  private String ariDate;

  /**
   * 评分人数
   */
  private String ratingCount;

  /**
   * 更新时间
   */
  private String lastUpdateAt;

  /**
   * 总集数
   */
  private Integer totalEpisode;

  /**
   * 播放平台
   */
  private String platform;

  /**
   * 国家/地区
   */
  private String country;


  /**
   * 角色列表
   */
  private String role;
  /**
   * 语言
   */
  private String language;
  /**
   * 编剧
   */
  private String screenwriter;
  /**
   * 片长
   */
  private String duration;
}
