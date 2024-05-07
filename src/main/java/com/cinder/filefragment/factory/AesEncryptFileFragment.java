package com.cinder.filefragment.factory;

import com.cinder.filefragment.threadPool.CallbackFunction;
import com.cinder.filefragment.threadPool.EncryptThreadPoolFactory;
import com.cinder.filefragment.threadPool.ReadFileThreadPoolFactory;
import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.cinder.filefragment.constants.BaseConfig.stepLength;

/**
 * @author cinder
 */
public class AesEncryptFileFragment extends AbstractFileFragment{
  public AesEncryptFileFragment(TimerCollector timerCollector) {
    super(timerCollector);
  }

  public void readEncryptAndLoad(String input, String output, String password) throws FileNotFoundException {
    super.read(input, output, password, (file, callbackFunction, timerCollector) -> {
      EncryptThreadPoolFactory.encrypt(file,callbackFunction,timerCollector);
    });
  }
}
