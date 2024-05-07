package com.cinder.filefragment.threadPool;

import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.sax.SAXSource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BlockWhenQueueFull implements RejectedExecutionHandler {
    private static AtomicInteger index = new AtomicInteger(0);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            BlockingQueue<Runnable> queue = executor.getQueue();
            if (!executor.isShutdown()) {
//                log.error("reject！！！！"+index.incrementAndGet());
                queue.put(r);  // 阻塞当前线程直到队列有空间
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Interrupted while submitting task", e);
        }
    }
}
