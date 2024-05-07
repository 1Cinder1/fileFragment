package com.cinder.filefragment.threadPool;

import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * @author cinder
 */
@Slf4j
@Data
public class ReadTask implements Runnable{
  private FragmentFile file;
  private byte[] bytes;
  private CallbackFunction callbackFunction;
  private TimerCollector timerCollector;

  public ReadTask(FragmentFile file, CallbackFunction callbackFunction, TimerCollector timerCollector) {
    this.file = file;
    this.bytes = new byte[file.getLength()];
    this.callbackFunction = callbackFunction;
    this.timerCollector = timerCollector;
  }
  @Override
  public void run() {
    if (Objects.nonNull(timerCollector.getReadTimerCollector())) {
      timerCollector.getReadTimerCollector().getStartTime().set(file.getIndex(),System.currentTimeMillis());
    }
    readWithoutMemOut();
    if (Objects.nonNull(timerCollector.getReadTimerCollector())) {
      Long start = timerCollector.getReadTimerCollector().getStartTime().get(file.getIndex());
      timerCollector.getReadTimerCollector().getTotalTime().set(file.getIndex(),System.currentTimeMillis()-start);
    }
  }
  private void readWithoutMemOut() {
    try {
      RandomAccessFile randomAccessFile = new RandomAccessFile(file.getFile(),"r");
      randomAccessFile.seek(file.getStart());
      int read = randomAccessFile.read(bytes);
      if (read != bytes.length) {
        log.error("read error,index: "+file.getIndex()+" real read: "+read+" length: "+bytes.length);
      }
      file.setData(bytes);
      if (this.callbackFunction != null) {
        callbackFunction.execute(file,null,timerCollector);
      }
    }catch (IndexOutOfBoundsException e) {
      log.error("indexOutOfBounds,bytes: "+bytes.length +", start: "+file.getStart()+", length: "+file.getLength());
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
  private void read() {
    try {
//                System.out.println("共进入run1：" + j.incrementAndGet());
      InputStream inputStream = file.getInputStream();
//                if (k.get() >= 260) {
//                    System.out.println(260);
//                }
//                System.out.println("共进入run2：" + k.incrementAndGet() + "  len:" + file.getLength());
//                System.out.println("共进入run3：" + l.incrementAndGet());
      long skip = inputStream.skip(file.getStart());
      if (skip != file.getStart()) {
        log.error("skip error,index: "+file.getIndex()+" real skip:"+skip);
      }
      int read = inputStream.read(bytes);
      if (read != bytes.length) {
        log.error("read error,index: "+file.getIndex()+" real read: "+read+" start: "+file.getStart());
      }
      System.out.println(file.getStart());

      file.setData(bytes);
//      new EncryptThreadPoolFactory().encrypt(file);
    }catch (IndexOutOfBoundsException e) {
      log.error("indexOutOfBounds,bytes: "+bytes.length +", start: "+file.getStart()+", length: "+file.getLength());
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
