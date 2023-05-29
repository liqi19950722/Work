package com.acme.biz.faulttolerance;

import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException;
import org.redisson.api.RSemaphore;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyBulkHead {
    RSemaphore semaphore;

    ExecutorService threadPoolExecutor;

    public MyBulkHead(RSemaphore semaphore, int maximumNumberOfConcurrent) {
        this.semaphore = semaphore;
        semaphore.trySetPermits(maximumNumberOfConcurrent);
    }

    public MyBulkHead(int maximumNumberOfConcurrent, int waitingTaskQueueSize) {
        this.threadPoolExecutor = new ThreadPoolExecutor(maximumNumberOfConcurrent, maximumNumberOfConcurrent,
                Duration.ofMillis(20).toMillis(), TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(waitingTaskQueueSize),
                (action, executor) -> {
                    throw new BulkheadException();
                });
    }

    public void semaphoreStyle(Runnable service) {
        var acquired = semaphore.tryAcquire(1);
        if (acquired) {
            try {
                service.run();
            } finally {
                semaphore.release();
            }
        } else {
            throw new BulkheadException();
        }
    }

    public void submit(BulkHeadStyle bulkHeadStyle, Runnable action) {
        switch (bulkHeadStyle) {
            case THREAD_POOL -> threadPoolStyle(action);
            case SEMAPHORE -> semaphoreStyle(action);
            default -> throw new IllegalStateException();
        }
    }

    public CompletableFuture<Void> threadPoolStyle(Runnable action) {
        return CompletableFuture.runAsync(action, threadPoolExecutor);
    }

    enum BulkHeadStyle {
        THREAD_POOL, SEMAPHORE;
    }
}
