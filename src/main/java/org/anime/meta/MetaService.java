package org.anime.meta;

import org.anime.entity.animation.Animation;
import org.anime.entity.animation.Schedule;
import org.anime.entity.bangmi.EpisodeResult;

import java.io.Serializable;
import java.util.List;

public interface MetaService extends Serializable {
  List<Animation> fetchSearchResultSync(String keyword, Integer page, Integer size) throws Exception;

  String fetchDailyRecommendSync() throws Exception;

  List<Schedule> fetchWeeklyUpdateSync() throws Exception;

  EpisodeResult fetchEpisodeSync(String subjectId) throws Exception;

  Animation fetchSubjectSync(Integer subjectId) throws Exception;
}
