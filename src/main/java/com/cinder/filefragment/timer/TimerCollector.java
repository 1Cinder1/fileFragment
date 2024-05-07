package com.cinder.filefragment.timer;

import lombok.Getter;

/**
 * @author cinder
 */
@Getter
public class TimerCollector {
  private WholeTimerCollector wholeTimerCollector;
  private WriteTimerCollector writeTimerCollector;
  private ReadTimerCollector readTimerCollector;
  private EncryptTimerCollector encryptTimerCollector;

  public TimerCollector setWholeTimerCollector(WholeTimerCollector wholeTimerCollector) {
    this.wholeTimerCollector = wholeTimerCollector;
    return this;
  }

  public TimerCollector setWriteTimerCollector(WriteTimerCollector writeTimerCollector) {
    this.writeTimerCollector = writeTimerCollector;
    return this;
  }

  public TimerCollector setReadTimerCollector(ReadTimerCollector readTimerCollector) {
    this.readTimerCollector = readTimerCollector;
    return this;
  }

  public TimerCollector setEncryptTimerCollector(EncryptTimerCollector encryptTimerCollector) {
    this.encryptTimerCollector = encryptTimerCollector;
    return this;
  }
}
