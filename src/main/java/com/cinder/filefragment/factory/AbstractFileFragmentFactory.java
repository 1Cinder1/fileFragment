package com.cinder.filefragment.factory;

import com.cinder.filefragment.timer.TimerCollector;


/**
 * @author cinder
 * 抽象文件分片工厂，用来创建各个具体的类，比如无加密的文件传输、AES加密的、RSA加密的代码工厂，具体的代码工厂继承AbstractFileFragment，使用内部基本的方法
 */
public abstract class AbstractFileFragmentFactory {
  abstract AbstractFileFragment newInstance(String str);
  TimerCollector timerCollector;
  void setTimerCollector(TimerCollector timerCollector) {
    this.timerCollector=timerCollector;
  };
}
