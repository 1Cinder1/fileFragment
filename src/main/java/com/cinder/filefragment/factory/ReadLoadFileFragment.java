package com.cinder.filefragment.factory;

import com.cinder.filefragment.threadPool.EncryptThreadPoolFactory;
import com.cinder.filefragment.threadPool.ReadFileThreadPoolFactory;
import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.utils.FragmentManager;
import com.cinder.filefragment.vo.FragmentFile;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.cinder.filefragment.constants.BaseConfig.stepLength;

/**
 * @author cinder
 * 对大文件仅做read并上传
 */
@Slf4j
public class ReadLoadFileFragment extends AbstractFileFragment{
  public ReadLoadFileFragment(TimerCollector timerCollector) {
    super(timerCollector);
  }
  public void readAndLoad(String input, String output) throws FileNotFoundException {
    super.read(input, output,"",(file,callbackFunction,timerCollector)->{
      FragmentManager.addPart(file,timerCollector);
    });
  }

}
