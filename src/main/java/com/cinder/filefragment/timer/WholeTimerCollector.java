package com.cinder.filefragment.timer;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cinder
 */
public class WholeTimerCollector extends AbstractTimerCollector{
  public WholeTimerCollector(Long total) {
    super(total);
  }

  public WholeTimerCollector() {
    super();
  }
}
