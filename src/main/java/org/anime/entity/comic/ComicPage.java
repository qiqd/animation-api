package org.anime.entity.comic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicPage implements Serializable {
  private String id;
  private String imageUrl;
  private Integer pageNumber;
  private String chapterId;
  private String title;
}