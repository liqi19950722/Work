package com.acme.biz.faulttolerance;

import org.eclipse.microprofile.faulttolerance.exceptions.BulkheadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static com.acme.biz.faulttolerance.MyBulkHead.BulkHeadStyle.SEMAPHORE;
import static com.acme.biz.faulttolerance.MyBulkHead.BulkHeadStyle.THREAD_POOL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Testcontainers
public class BulkHeadTest {

    // type
    // 1. thread pool
    //   throw BulkheadException when cannot be added to the waiting queue
    // 2. semaphore
    //   throw BulkheadException on reaching maximum request counter

    @Container
    public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
    RedissonClient redisson;
    String resourceName = "service";
    int maximumNumberOfConcurrent = 1;
    int waitingTaskQueueSize = 1;

    MyBulkHead semaphoreBulkHead;
    MyBulkHead threadPoolBulkHead;
    Runnable action;

    @BeforeEach
    public void setup() {
        var host = redis.getHost();
        var port = redis.getFirstMappedPort();
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port);

        this.redisson = Redisson.create(config);
        this.resourceName = "service";
        this.semaphoreBulkHead = new MyBulkHead(redisson.getSemaphore(resourceName), maximumNumberOfConcurrent);
        this.threadPoolBulkHead = new MyBulkHead(maximumNumberOfConcurrent, waitingTaskQueueSize);
        this.action = mock(Runnable.class);
    }

    @Nested
    class Semaphore {
        @Test
        public void should_use_redisson_semaphore() {
            semaphoreBulkHead.submit(SEMAPHORE, action);

            var semaphore = redisson.getSemaphore(resourceName);
            assertEquals(1, semaphore.drainPermits());
            verify(action, times(1)).run();
        }

        @Test
        public void should_throw_BulkHeadException_when_not_acquired() throws InterruptedException {
            var semaphore = redisson.getSemaphore(resourceName);
            semaphore.acquire();

            assertThrows(BulkheadException.class, () -> semaphoreBulkHead.submit(SEMAPHORE, action));
        }
    }

    @Nested
    class ThreadPool {
        @Test
        public void should_use_thread_pool() {
            var completableFuture = threadPoolBulkHead.threadPoolStyle(action);

            completableFuture.whenComplete((result, exception) -> {
                verify(action, times(1)).run();
            });

            completableFuture.join();
        }

        @Test
        public void should_throw_BulkHeadException_when_cannot_be_added_to_the_waiting_queue() {
            Runnable foreverLoop = () -> {
                for (; ; ) {
                    try {
                        TimeUnit.SECONDS.sleep(10L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            threadPoolBulkHead.submit(THREAD_POOL, foreverLoop);
            threadPoolBulkHead.submit(THREAD_POOL, foreverLoop);

            assertThrows(BulkheadException.class, () -> threadPoolBulkHead.threadPoolStyle(action));
        }
    }
}
