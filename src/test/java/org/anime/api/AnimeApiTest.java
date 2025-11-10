package org.anime.api;

import org.anime.entity.bangmi.SourceWithDelay;
import org.junit.Test;

public class AnimeApiTest {
  @Test
  public void testGetSources() throws InterruptedException {
    AnimeApi.initialization();
    Thread.sleep(15000);
    for (SourceWithDelay sourceWithDelay : AnimeApi.SOURCES_WITH_DELAY) {
      System.out.println("delay:" + sourceWithDelay.getDelay() + "name:" + sourceWithDelay.getHtmlParser().getName());
    }
  }
}