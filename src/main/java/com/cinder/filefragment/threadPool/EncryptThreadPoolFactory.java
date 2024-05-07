package com.cinder.filefragment.threadPool;

import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @author cinder
 */
@Slf4j
public class EncryptThreadPoolFactory {
    private static ArrayBlockingQueue arrayBlockingQueue =  new ArrayBlockingQueue<>(100);

    public static AtomicInteger queueCount = new AtomicInteger(0);
    public static AtomicInteger i = new AtomicInteger(0);
    private static ThreadPoolExecutor encryptThreadPool = new ThreadPoolExecutor(4,Runtime.getRuntime().availableProcessors()+1,
            1000, TimeUnit.SECONDS,arrayBlockingQueue,new BlockWhenQueueFull());
    public static void encrypt(FragmentFile file, CallbackFunction callbackFunction, TimerCollector timerCollector) {
        encryptThreadPool.submit(new EncryptTask(file,timerCollector));
//        System.out.println("当前加密任务提交共："+i.incrementAndGet());
    }
}
