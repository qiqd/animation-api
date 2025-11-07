package org.animation.service.impl;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.animation.entity.Animation;

import java.util.List;

public class AiyiFanTest extends TestCase {
  private final AiyiFan aiyiFan = new AiyiFan();

  public void testFetchSearchSync() throws Exception {
    List<Animation> animations = aiyiFan.fetchSearchSync("反叛的鲁路修", 1, 10);
    System.out.println(JSON.toJSONString(animations));
  }

  public void testFetchDetailSync() throws Exception {
    System.out.println(JSON.toJSONString(aiyiFan.fetchDetailSync("/ayf.sbs-vod/54456.html")));
  }

  public void testFetchPlayInfoSync() throws Exception {
    System.out.println(JSON.toJSONString(aiyiFan.fetchPlayInfoSync("/ayf.sbs-play/54456-1-1.html")));
  }
}