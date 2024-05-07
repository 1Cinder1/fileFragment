package com.cinder.filefragment.utils;

import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;
import com.cinder.filefragment.vo.FragmentWrite2File;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;



@Slf4j
public class FragmentManager {
    private static ConcurrentHashMap<String, FragmentWrite2File> fileParts = new ConcurrentHashMap<>();
    public static AtomicInteger index = new AtomicInteger(0);

    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static boolean isNext = false;
    private static AtomicInteger queueCountPrio = new AtomicInteger(0);

    public static void addPart(FragmentFile file,TimerCollector timerCollector) throws IOException {
        fileParts.computeIfAbsent(file.getFileName(),
                        k->new FragmentWrite2File(new PriorityBlockingQueue<>(100,(p1,p2)->Integer.compare(p1.getIndex(),p2.getIndex())))
                ).getPriorityBlockingQueue().add(file);

        FragmentWrite2File fragmentWrite2File = fileParts.get(file.getFileName());
//        if (isNext) {
//            log.info("队列放进去,目前数量："+queueCount.get());
//            log.error("此时map中优先队列的size："+queueCountPrio.incrementAndGet());
//        }
        if (fragmentWrite2File.getCurrentIndex().get() == fragmentWrite2File.getPriorityBlockingQueue().peek().getIndex()) {
            assembleFile(file.getFileName(),timerCollector);
        }
    }
    private static void assembleFile(String fileName,TimerCollector timerCollector) throws IOException {
        FragmentWrite2File fragmentWrite2File = fileParts.get(fileName);
        AtomicInteger currentIndex = fragmentWrite2File.getCurrentIndex();
        PriorityBlockingQueue<FragmentFile> priorityBlockingQueue = fragmentWrite2File.getPriorityBlockingQueue();
        try {
            reentrantLock.lock();
            while (!priorityBlockingQueue.isEmpty() && currentIndex.get() == priorityBlockingQueue.peek().getIndex()) {


                currentIndex.getAndIncrement();
                FragmentFile file = priorityBlockingQueue.poll();
                if (Objects.nonNull(timerCollector.getWriteTimerCollector())) {
                    timerCollector.getWriteTimerCollector().getStartTime().set(file.getIndex(),System.currentTimeMillis());
                }
                assert file != null;
                file.getOutputStream().write(file.getData(),0,file.getLength());
                if (Objects.nonNull(timerCollector.getWriteTimerCollector())) {
                    Long start = timerCollector.getWriteTimerCollector().getStartTime().get(file.getIndex());
                    timerCollector.getWriteTimerCollector().getTotalTime().set(file.getIndex(),System.currentTimeMillis()-start);
                }
                if (Objects.nonNull(timerCollector.getWholeTimerCollector())) {
                    Long start = timerCollector.getWholeTimerCollector().getStartTime().get(file.getIndex());
                    timerCollector.getWholeTimerCollector().getTotalTime().set(file.getIndex(),System.currentTimeMillis()-start);
                }
                if (file.getTotal().equals(currentIndex.get())) {
                    file.getInputStream().close();
                    file.getOutputStream().close();
//                    MAP.put(file.getFileName(),System.currentTimeMillis()-MAP.getOrDefault(file.getFileName(),Long.MAX_VALUE));
                    log.info("完成第: "+index.incrementAndGet()+"个文件");
                }
                log.info("完成index："+file.getIndex());
            }
        }finally {
            reentrantLock.unlock();
        }
    }
}
