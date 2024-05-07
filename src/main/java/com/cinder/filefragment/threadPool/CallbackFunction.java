package com.cinder.filefragment.threadPool;

import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;

import java.io.IOException;

/**
 * @author cinder
 */
public interface CallbackFunction {
  void execute(FragmentFile file, CallbackFunction callbackFunction, TimerCollector timerCollector) throws IOException;
}
