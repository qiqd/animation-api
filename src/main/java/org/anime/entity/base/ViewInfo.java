package org.anime.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewInfo implements Serializable {
  private String episodeName;
  private String episodeId;
  private List<String> urls;
}
