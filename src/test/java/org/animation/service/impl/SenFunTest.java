package org.animation.service.impl;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.animation.entity.PlayInfo;

public class SenFunTest extends TestCase {
  private final static SenFun senFun = new SenFun();

  public void testFetchSearchSync() throws Exception {
    System.out.println(JSON.toJSONString(senFun.fetchSearchSync("jojo", 1, 10)));
  }

  public void testFetchDetailSync() throws Exception {
    System.out.println(JSON.toJSONString(senFun.fetchDetailSync("/voddetail/1993743391.html")));
  }

  public void testFetchPlayInfoSync() throws Exception {
    PlayInfo playInfo = senFun.fetchPlayInfoSync("/vodwatch/1993743391/ep4.html");
    System.out.println(JSON.toJSONString(playInfo));
  }
}