package org.anime.entity.comic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicDetail implements Serializable {
  private Comic comic;
  private List<Chapter> chapters;
}