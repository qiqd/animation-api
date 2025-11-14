package org.anime.parser;

import org.anime.entity.base.Detail;
import org.anime.entity.base.ViewInfo;
import org.anime.entity.comic.Comic;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractComicParser implements HtmlParser, Serializable {
  @Override
  public abstract List<Comic> fetchSearchSync(String keyword, Integer page, Integer size) throws Exception;

  @Nullable
  @Override
  public abstract Detail<Comic> fetchDetailSync(String mediaId) throws Exception;

  @Nullable
  @Override
  public abstract ViewInfo fetchViewSync(String episodeId) throws Exception;
}
