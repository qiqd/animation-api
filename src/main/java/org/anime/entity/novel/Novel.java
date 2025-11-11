package org.anime.entity.novel;

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
public class Novel extends Media implements Serializable {
  private String publisher;
  private String publicationDate;
  private String isbn;
  private String pages;
  private String chapters;
  private String wordCount;
  private String volume;
  private String latestChapter;
}
