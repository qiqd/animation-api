package org.animation.service.impl;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.animation.entity.Animation;
import org.animation.entity.AnimationDetail;
import org.animation.entity.PlayInfo;

import java.util.List;

public class MengDaoTest extends TestCase {
  private final MengDao mengDao = new MengDao();

  public void testFetchSearchSync() throws Exception {
    List<Animation> animations = mengDao.fetchSearchSync("JOJO的奇妙冒险", 1, 10);
    System.out.println(JSON.toJSONString(animations));
  }

  public void testFetchDetailSync() throws Exception {
    AnimationDetail animationDetail = mengDao.fetchDetailSync("/man/99232.html");
    System.out.println(JSON.toJSONString(animationDetail));
  }

  public void testFetchPlayInfoSync() throws Exception {
    PlayInfo playInfo = mengDao.fetchPlayInfoSync("/man_v/9232-0-0.html");
    System.out.println(JSON.toJSONString(playInfo));
  }
}