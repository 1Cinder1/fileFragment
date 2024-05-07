package com.cinder.filefragment.timer;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cinder
 */
@Data
public abstract class AbstractTimerCollector {
  public Long total;
  public List<Long> startTime;
  public List<Long> totalTime;
  public AbstractTimerCollector(Long total) {
    this.total = total;
    this.startTime = new ArrayList<>(total.intValue());
    this.totalTime = new ArrayList<>(total.intValue());
  }
  public AbstractTimerCollector(){

  }
  public void setTotal(Long total) {
    this.total = total;
    Long[] longs = new Long[total.intValue()];
    Arrays.fill(longs,new Long(0));
    this.startTime = Arrays.asList(longs);
    this.totalTime = Arrays.asList(longs);
  }
}
