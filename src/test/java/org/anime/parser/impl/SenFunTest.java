package org.anime.parser.impl;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.anime.entity.base.ViewInfo;
import org.anime.parser.impl.animation.SenFun;

public class SenFunTest extends TestCase {
  private final static SenFun senFun = new SenFun();

  public void testFetchSearchSync() throws Exception {
    System.out.println(JSON.toJSONString(senFun.fetchSearchSync("jojo", 1, 10)));
  }

  public void testFetchDetailSync() throws Exception {
    System.out.println(JSON.toJSONString(senFun.fetchDetailSync("/voddetail/1993743391.html")));
  }

  public void testFetchViewSync() throws Exception {
    ViewInfo playInfo = senFun.fetchViewSync("/vodwatch/1993743391/ep4.html");
    System.out.println(JSON.toJSONString(playInfo));
  }
}