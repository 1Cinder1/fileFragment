package com.cinder.filefragment.vo;

import lombok.Data;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @author cinder
 */
@Data
public class FragmentWrite2File {
    private volatile PriorityBlockingQueue<FragmentFile> priorityBlockingQueue;
    private AtomicInteger currentIndex;

    public FragmentWrite2File(PriorityBlockingQueue<FragmentFile> priorityBlockingQueue) {
        this.priorityBlockingQueue = priorityBlockingQueue;
        this.currentIndex = new AtomicInteger(0);
    }
}
