package org.anime.entity.animation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayInfo implements Serializable {
  private String id;
  private String playUri;
}
