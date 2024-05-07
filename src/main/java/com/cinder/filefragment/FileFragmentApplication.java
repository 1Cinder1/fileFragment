package com.cinder.filefragment;

import com.cinder.filefragment.factory.ReadLoadFileFragment;
import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.timer.WholeTimerCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

//@SpringBootApplication
public class FileFragmentApplication {
  private static String basePath = "/Users/cinder/code/java/hyperchain/";

  public static void main(String[] args) throws FileNotFoundException {
//    SpringApplication.run(FileFragmentApplication.class, args);
    ReadLoadFileFragment readLoadFileFragment = new ReadLoadFileFragment(new TimerCollector()
      .setWholeTimerCollector(new WholeTimerCollector()));
    readLoadFileFragment.readAndLoad(basePath+"1.33G_ppt&video.zip",basePath+"output.zip");
  }

}
