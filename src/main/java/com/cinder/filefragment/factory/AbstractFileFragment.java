package com.cinder.filefragment.factory;

import com.cinder.filefragment.threadPool.CallbackFunction;
import com.cinder.filefragment.threadPool.ReadFileThreadPoolFactory;
import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static com.cinder.filefragment.constants.BaseConfig.stepLength;

/**
 * @author cinder
 * 抽象分片，创建基本的父类，具体的类继承该父类
 */
public abstract class AbstractFileFragment {
  TimerCollector timerCollector;
  public AbstractFileFragment() {}
  public AbstractFileFragment(TimerCollector timerCollector) {
    this.timerCollector = timerCollector;
  }
  protected void read(String input, String output,String password,CallbackFunction callbackFunction) throws FileNotFoundException {
    // 读取待加密的文件
    Path inputPath = Paths.get(input);
    Path outputPath = Paths.get(output);

    // 创建输入流和输出流
    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputPath.toFile()));
    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()));

    BigDecimal available = new BigDecimal(inputPath.toFile().length());
    BigDecimal step = new BigDecimal(stepLength);
    int total = available.divide(step).setScale(0, BigDecimal.ROUND_UP).intValue();
    setTimerTotal((long) total);
    input = input+ UUID.randomUUID();
    for(int i=0;i<total;i++) {
      if (Objects.nonNull(this.timerCollector.getWholeTimerCollector())) {
        this.timerCollector.getWholeTimerCollector().getStartTime().set(i,System.currentTimeMillis());
      }
      FragmentFile fragmentFile;
      if (i==total-1) {
        fragmentFile = new FragmentFile(input,(int) i,total,(long)i*stepLength,available.intValue()-(int) i*stepLength,inputPath.toFile(),outputStream,password);
      }else {
        fragmentFile = new FragmentFile(input,(int) i,total,(long)i*stepLength,stepLength,inputPath.toFile(),outputStream,password);
      }
      ReadFileThreadPoolFactory.submitTask(fragmentFile,callbackFunction,this.timerCollector);
    }
  }
  public void setTimerTotal(Long total) {
    if (Objects.nonNull(this.timerCollector.getEncryptTimerCollector())) {
      this.timerCollector.getEncryptTimerCollector().setTotal(total);
    }
    if (Objects.nonNull(this.timerCollector.getReadTimerCollector())) {
      this.timerCollector.getReadTimerCollector().setTotal(total);
    }
    if (Objects.nonNull(this.timerCollector.getWriteTimerCollector())) {
      this.timerCollector.getWriteTimerCollector().setTotal(total);
    }
    if (Objects.nonNull(this.timerCollector.getWholeTimerCollector())) {
      this.timerCollector.getWholeTimerCollector().setTotal(total);
    }
  }
}
