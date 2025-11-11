package org.anime.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detail<T extends Media> implements Serializable {
  private T media;
  private List<Source> episodes;
}
