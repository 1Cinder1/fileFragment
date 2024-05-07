package com.cinder.filefragment.threadPool;
import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author cinder
 */
@Slf4j
public class ReadFileThreadPoolFactory {
    private static String filename = "";
    private static AtomicInteger i = new AtomicInteger(0);
    private static AtomicInteger j = new AtomicInteger(0);
    private static AtomicInteger k = new AtomicInteger(0);
    private static AtomicInteger l = new AtomicInteger(0);
    private static AtomicInteger info = new AtomicInteger(0);
    private static ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<>(100);
    private static ThreadPoolExecutor readThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()+3,
      Runtime.getRuntime().availableProcessors()*2, 1000, TimeUnit.SECONDS, arrayBlockingQueue, new BlockWhenQueueFull());

    public static void submitTask(FragmentFile file, CallbackFunction callbackFunction, TimerCollector timerCollector) {
        readThreadPool.submit(new ReadTask(file,callbackFunction,timerCollector));
//        System.out.println("当前提交任务共：" + i.incrementAndGet());
    }


}
